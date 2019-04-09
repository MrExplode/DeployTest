# Travis things
fold_start() {
  echo -e "travis_fold:start:$1\033[33;1m$2\033[0m"
}

fold_end() {
  echo -e "\ntravis_fold:end:$1\r"
}

###################################
# Configuration

PROJECT_NAME="DeployTest"
PROJECT_AUTHOR="MrExplode"
PROJECT_HOME=$HOME/build/$PROJECT_AUTHOR/$PROJECT_NAME
###################################

###################################
# Beginning of the script
###################################

# Building the javadoc
fold_start doc "Building JavaDoc"
mvn javadoc:javadoc
fold_end doc

# Setting up git
cd $HOME
git config --global user.name "ExplodeBot"
git config --global user.email "sunstorm@outlook.hu"

# Cloning webpage
git clone --branch=master https://${GITHUB_TOKEN}@github.com/MrExplode/MrExplode.github.io website

# removing old JD from index, copying new one, adding to index, committing and then pushing
cd website
git rm -rf projects/$PROJECT_NAME/javadoc
cp -Rf $PROJECT_HOME/target/apidocs projects/$PROJECT_NAME/javadoc
git add -f .
git commit -m "Latest JavaDoc for $PROJECT_NAME #$TRAVIS_BUILD_NUMBER
Latest JavaDoc on a successful Travis CI build, pushed automatically"
echo -e "\e[93mPushing JavaDoc to webpage"
git push -fq origin master

exit 0
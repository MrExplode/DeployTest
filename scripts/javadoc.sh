# Travis things
fold_start() {
  echo -e "travis_fold:start:$1\033[33;1m$2\033[0m"
}

fold_end() {
  echo -e "\ntravis_fold:end:$1\r"
}

PROJECT_NAME="DeployTest"

###################################
# Beginning of the script
###################################
echo -e "\nBuilding JavaDoc...\n"

# Building the javadoc
mvn javadoc:javadoc

# Setting up git
cd $HOME
git config --global user.name "ExplodeBot"
git config --global user.email "sunstorm@outlook.hu"

# Cloning webpage
fold_start clone "cloning webpage"
git clone --branch=master https://${GITHUB_TOKEN}@github.com/MrExplode/MrExplode.github.io website
fold_end clone

cd website
git rm -rf ./projects/$PROJECT_NAME/javadoc
cp -Rf $HOME/target/apidocs ./projects/$PROJECT_NAME/javadoc
git add -f .
git commit -m "Latest JavaDoc for $PROJECT_NAME #$TRAVIS_BUILD_NUMBER
Latest JavaDoc on a successful Travis CI build, pushed automatically"
fold_start push "pushing changes"
git push -fq origin master
fold_end push

exit 0
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
echo -e "\n\e[93mBuilding JavaDoc...\n"

# Building the javadoc
fold_start doc "Building javadoc"
mvn javadoc:javadoc
fold_end doc

# Setting up git
cd $HOME
git config --global user.name "ExplodeBot"
git config --global user.email "sunstorm@outlook.hu"

# Cloning webpage
git clone --branch=master https://${GITHUB_TOKEN}@github.com/MrExplode/MrExplode.github.io website

# seek into target folder
cd website
git rm -rf projects/$PROJECT_NAME/javadoc
cp -Rf $HOME/target/apidocs projects/$PROJECT_NAME/javadoc
git add -f .
git commit -m "Latest JavaDoc for $PROJECT_NAME #$TRAVIS_BUILD_NUMBER
Latest JavaDoc on a successful Travis CI build, pushed automatically"
echo -e "\e[93mPushing JavaDoc to webpage"
git push -fq origin master
ls -d $HOME
echo "2:"
ls $HOME
echo "list for target"
ls -d $HOME/target
echo "2:"
ls $HOME/target

exit 0
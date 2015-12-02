#!/bin/sh
#
# usage: update-version.sh new-version
#
# this will update the source code to refer to the new version
# in both the plugin and the template

if test -d ../frege-lein-plugin
then
    echo "Found plugin."
else
    echo "error: This should be run in the frege-lein-plugin folder."
    exit 1
fi
if test -d ../frege-lein-template
then
    echo "Found template."
else
    echo "error: frege-lein-template folder not found (should be next to plugin)."
    exit 1
fi

if test "x$1" = "x"
then
    echo "usage: update-version.sh new-version"
    exit 1
fi
new_version="$1"
new_major=`echo $new_version | sed 's;-.*;;'`
new_minor=`echo $new_version | sed 's;.*-;;'`
if test "x$new_major" != "x" -a "x$new_minor" != "x" -a "x$new_major" != "x$new_minor"
then
    echo "Will update to ${new_major}-${new_minor} version."
else
    echo "error: new-version does not appear to be x.y.z-sha."
    exit 1
fi


old_version=`fgrep 'private fregec-version' src/leiningen/fregec.clj | sed 's;.*"\(.*\)".*;\1;'`
if test "x$old_version" = "x"
then
    echo "error: Cannot find current Frege version (in src/leiningen/fregec.clj)."
    exit 1
else
    echo "Found current version: ${old_version}."
fi

old_major=`echo $old_version | sed 's;-.*;;'`

files=`find . ../frege-lein-template -type f -exec fgrep -l $old_version {} \;`
for f in $files
do
    echo "Updating $f..."
    cp $f $f.update_version
    sed "s;$old_version;$new_version;g" < $f.update_version > $f
done
find . ../frege-lein-template -name '*.update_version' -delete

files=`find . ../frege-lein-template -type f -exec fgrep -l $old_major {} \;`
for f in $files
do
    echo "Updating $f..."
    cp $f $f.update_version
    sed "s;$old_major;$new_major;g" < $f.update_version > $f
done
find . ../frege-lein-template -name '*.update_version' -delete

echo "You should now review and commit the changes."

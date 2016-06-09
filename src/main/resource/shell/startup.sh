#!/bin/bash

# java command
JAVA="$(which java)"

# directory
CURRENTPATH="$(pwd)"
APPDIR="$(dirname "$CURRENTPATH")/deer"

# run with heapsize
JAVA_HEAP_MAX=-Xmx1000m

# loading the library
for jar in `ls $APPDIR/lib/*.jar`
do
    CLASSPATH="$CLASSPATH:""$jar"
done
echo $CLASSPATH
CLASSPATH="$CLASSPATH:$APPDIR/conf:$APPDIR/data:."

# main class
CLASS=com.havens.siamese.Daemon

#
EXEC_CALL="$JAVA $JAVA_HEAP_MAX -classpath $CLASSPATH"

#java -Xss1024k -Xmn256m -Xms512m -Xmx1024m -cp lib/*:. com.havens.siamese.Daemon
#java -Xss1024k -Xmn256m -Xms512m -Xmx1024m -cp $CLASSPATH:$APPDIR:.
echo $EXEC_CALL $CLASS
exec $EXEC_CALL $CLASS "$@"

#!/bin/bash 
# 
# chkconfig: - 91 35 
# description: Starts and stops deer
# cp eagle /etc/init.d
# chkconfig --add deer
# chkconfig deer on
# service deer start
# service deer stop


DEER_HOME=/home/havens/App/deer/
PID_FILE=/var/run/deer8000.pid
LOCK_FILE=/var/lock/subsys/deer8000
prog=deer
JAVA="$(which java)"
RETVAL=0 

STOP_TIMEOUT=${STOP_TIMEOUT-10}

APPDIR="/home/havens/App/deer"

# run with heapsize
JAVA_HEAP_MAX=-Xmx1024m
JAVA_HEAP_MIN=-Xms512m
JAVA_THREAD_SIZE=-Xss1m
JAVA_TIMEZONE=-Duser.timezone=GMT+08

# match CPU
JAVA_GCTHREADS=-XX:ParallelGCThreads=8

# java options
JAVA_OPT="$JAVA_HEAP_MIN $JAVA_HEAP_MAX -Xmn2g $JAVA_THREAD_SIZE -XX:MaxPermSize=256m -XX:SurvivorRatio=4 -XX:MaxTenuringThreshold=0 -XX:+UseConcMarkSweepGC $JAVA_GCTHREADS $JAVA_TIMEZONE"

# loading the library
for jar in `ls $APPDIR/lib/*.jar`
do
    CLASSPATH="$CLASSPATH:""$jar"
done

CLASSPATH="$CLASSPATH:$APPDIR/conf:$APPDIR/data:$APPDIR:."

# main class
CLASS=com.havens.siamese.Daemon


# Source function library. 
. /etc/rc.d/init.d/functions 
# Source networking configuration. 
. /etc/sysconfig/network 
# Check that networking is up. 
[ ${NETWORKING} = "no" ] && exit 0 

# Start daemons functions. 
start() { 
# Start eagle system 
	if [ -e $LOCK_FILE ];then 
		echo "$LOCK_FILE already running...." 
		exit 1 
	fi 

	echo -n $"Starting $LOCK_FILE: " 

        daemon --pidfile="$PID_FILE" $JAVA $JAVA_OPT -classpath $CLASSPATH $CLASS
	RETVAL=$? 
	echo 
	[ $RETVAL -eq 0 ] && touch $LOCK_FILE 
	return $RETVAL 
} 

# Stop daemons functions. 
stop() { 
# Stop eagle system
	echo -n $"Stopping $LOCK_FILE: " 
        killproc -p ${PID_FILE} -d ${STOP_TIMEOUT} $JAVA
	RETVAL=$? 
	echo 
	[ $RETVAL -eq 0 ] && rm -f $LOCK_FILE $PID_FILE 
	return $RETVAL 
}

# See how we were called. 
case "$1" in 
	start) 
	start 
	;; 
	stop) 
	stop 
	;; 
	restart) 
	stop 
	start 
	;; 
	*) 
	echo $"Usage: $prog {start|stop|restart}" 
	exit 1 
esac 

exit $RETVAL 

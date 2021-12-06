#!/bin/bash
JAVA_OPTS='-Xmx512m -Xms256m -Xmn64m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=1g'
port=30003
active_profile=prod

if [ $# -gt 0 ]
then
port=$1
fi

echo '启动端口: '${port}
logging_file=/root/scrm/log/${port}.log
spid=`ps -ef|grep 'server.port='${port} | awk '{print $2 $8}' | grep java`
spid=${spid/'java'/''}
if [ ${spid} ]
then
kill -9 ${spid}
if [ $? = 0 ]
  then
  echo '成功结束进程:'${spid}
else
  echo '进程结束失败'
fi
else
echo '不存在进程'
fi

jar_file=`ls ./|awk '/.*jar$/{print}'`

if [ ${jar_file} ]
  then
  echo 'find jar file'
  nohup java ${JAVA_OPTS} -jar ./${jar_file} --spring.profiles.active=${active_profile} --server.port=${port} --logging.file=${logging_file} > /dev/null 2>&1 &
  tailf ${logging_file}
else
  echo '未找到 target文件夹下的jar文件'
fi

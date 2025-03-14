#!/bin/bash


#弃用、被删除
cd /home/ecm
java -server -Xms512M -Xmx512M -Xss256k -jar /home/ecm/wechat.war &
sleep 5
result=$(netstat -na | grep 80 | wc -l)
if [ $result -gt 1 ];
then
	echo "启动成功..."
else
	echo"启动失败"
fi
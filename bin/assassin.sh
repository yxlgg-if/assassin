#!/bin/bash
# 启动脚本

### Usage:
###  ./assassin.sh [--server|--client] [Options]
### Options:
###   --help      使用帮助.
###   start       启动服务
###   stop        停止服务
###   restart     重启服务
###   --server    指定为服务端
###   --client    指定为客户端

# 获取当前目录
script_abs=`readlink -f "$0"`
script_dir=`dirname $script_abs`


args=(
start
stop
restart
)

abs=(
--server
--client
)

var1=$1
var2=$2
var3=$3

function help() {
    sed -rn 's/^### ?//;T;p;' "$0" && exit
}

function err() {
    echo -e "ERR:invalid argument!!!" && help
}

function check_jdk() {
    java -version || (echo "warning:未检测到jdk!!!" && exit)
}

function bin_start(){
    pid=`ps -ef|grep "java -jar ${script_dir}/lib/$1.jar"|grep -v "grep"|awk '{print $2}'`
    if [[ ${pid} ]]; then
        echo "warning:$1 started." && exit
    fi
    echo "$1 starting..."
    java -jar ${script_dir}/lib/$1.jar &> $1.log &
    echo "$1 started."
}

function bin_stop() {
    pid=`ps -ef|grep "java -jar ${script_dir}/lib/$1.jar"|grep -v "grep"|awk '{print $2}'`
    if [[ ${pid} ]]; then
        echo "$1[${pid}] closing..."
        kill -9 ${pid}
    fi
    echo "$1[${pid}] closed."
    sleep 1
}

function server_bin() {
    case $1 in
    start)
        bin_start $2
    ;;
    stop)
        bin_stop $2
    ;;
    restart)
        bin_stop $2
        bin_start $2
    ;;
    *)
        err
    ;;
    esac
}

function run() {
    argLen=${#args[@]}
    absLen=${#abs[@]}
    [[ ${var1} ]] && [[ ! ${var2} ]] && [[ ${var1} == '--help' ]] && help
    if [[ $argLen -ne $((`echo ${args[@]/${var1}/}|awk '{print NF}'`)) ]] && [[ $absLen -ne $((`echo ${abs[@]/${var2}/}|awk '{print NF}'`)) ]]; then
        [[ ${var2} == '--server' ]] && server_bin ${var1} 'AssassinServer' && exit
        [[ ${var2} == '--client' ]] && server_bin ${var1} 'AssassinClient' && exit
    elif [[ $argLen -ne $((`echo ${args[@]/${var2}/}|awk '{print NF}'`)) ]] && [[ $absLen -ne $((`echo ${abs[@]/${var1}/}|awk '{print NF}'`)) ]]; then
        [[ ${var1} == '--server' ]] && server_bin ${var2} 'AssassinServer' && exit
        [[ ${var1} == '--client' ]] && server_bin ${var2} 'AssassinClient' && exit
    else
        err
    fi
}

run

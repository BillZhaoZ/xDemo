# 命令格式 

echo "### 开始提交 ###"
git add .

git status
  
echo "### 添加文件 ###"
 
git commit -m "$2" 
 
echo "### 开始推送 ###"

if [ ! $1 ]
then
  echo "### 请输入自己提交代码的分支 ###"
  exit;
fi
 
git push origin "$1"
 
sleep 1s

echo "### 推送成功，开始编译 ###"
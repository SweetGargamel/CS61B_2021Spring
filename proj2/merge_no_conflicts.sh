#!/bin/bash

# ==============================================================================
# 这是一个根据 test33-merge-no-conflicts.in 自动生成的测试脚本。
# 它会逐行执行测试文件中的所有操作。
#
# 更新：所有的文件创建命令 (echo "content" > file) 已被替换为
#       创建空文件的命令 (touch file)。
# ==============================================================================

# 在每条命令执行前，打印命令并暂停，等待用户按回车。
# 如果想一次性全部运行，请注释掉下面这行。


# === 来自 prelude1.inc ===
java gitlet.Main init

# === 来自 setup1.inc ===
# 修改点：不再写入内容，仅创建空文件
touch f.txt
touch g.txt
java gitlet.Main add g.txt
java gitlet.Main add f.txt

# === 来自 setup2.inc ===
java gitlet.Main commit "Two files"


# === 来自 test33-merge-no-conflicts.in ===

# 1. 创建新分支 'other'
java gitlet.Main branch other

# 2. 在 master 分支上进行操作
# 修改点：不再写入内容，仅创建空文件
touch h.txt
java gitlet.Main add h.txt
java gitlet.Main rm g.txt
java gitlet.Main commit "Add h.txt and remove g.txt"

# 3. 切换到 'other' 分支并进行操作
java gitlet.Main checkout other
#java gitlet.Main status
#java gitlet.Main log
java gitlet.Main rm f.txt
# 修改点：不再写入内容，仅创建空文件
touch k.txt
java gitlet.Main add k.txt
java gitlet.Main commit "Add k.txt and remove f.txt"
#java gitlet.Main status
#java gitlet.Main log
# 4. 切换回 master 分支并合并
java gitlet.Main checkout master
#java gitlet.Main status
#java gitlet.Main log
trap 'read -p "即将执行: [$BASH_COMMAND] - 按回车键继续..."' DEBUG

java gitlet.Main merge other
java gitlet.Main status
java gitlet.Main log
# 清除 trap 钩子，让脚本干净地退出
trap - DEBUG
rm *.txt
rm -f -r .gitlet
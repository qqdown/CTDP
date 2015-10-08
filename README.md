Chinese-Text-Data-Processing
=======

##一、当前功能

1、将文件夹中所有的txt文件中的内容，按行读取，每一行作为一个post，对每一个post以所有txt中的内容作为全体计算tf-idf，输出为与原始txt及每一行对应的tf-idf。由于结果包含大量的0，所以采取了稀疏矩阵的存储方式。

##二、使用方法
1、打开\CTDP\CTDP文件夹

2、在该目录下，运行命令 java -jar CTDP.jar -f lily

3、在目录中会生成output文件夹，输出结果将会保存在这个文件夹中


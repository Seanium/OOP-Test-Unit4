import os
import shutil


def generate(fileNum, bookCmdCnt, otherCmdCnt):
    if os.path.exists("input"):
        shutil.rmtree("input")
    os.mkdir("input")
    for i in range(fileNum):
        os.system(
            f"java -jar out/artifacts/OOP_Test_Unit4_jar/OOP-Test-Unit4.jar {bookCmdCnt} {otherCmdCnt} > input/point{i}.txt")


def compare(fileNum, jarName1, jarName2):
    if os.path.exists("output1"):
        shutil.rmtree("output1")
    os.mkdir("output1")
    if os.path.exists("output2"):
        shutil.rmtree("output2")
    os.mkdir("output2")

    for i in range(fileNum):

        # run jar
        os.system(
            f"java -jar jar/{jarName1}.jar < input/point{i}.txt > output1/result{i}.txt")
        os.system(
            f"java -jar jar/{jarName2}.jar < input/point{i}.txt > output2/result{i}.txt")

        # open file
        with open(f"output1/result{i}.txt", 'r') as f1:
            list1 = f1.readlines()
        with open(f"output2/result{i}.txt", 'r') as f2:
            list2 = f2.readlines()

        # compare
        if len(list1) != len(list2):
            print(f"point {i} 文件总行数不等: {len(list1)}, {len(list2)}")
            continue

        flag = 1
        for j in range(len(list1)):
            if list1[j] == list2[j]:
                pass
            else:
                flag = 0
                print(f"point {i} diff in line: {j+1}")
                break
        if flag == 1:
            print(f"right in point{i}")


if __name__ == '__main__':
    fileNum = 10
    bookCmdCnt = 10
    otherCmdCnt = 20
    jarName1 = "hw13"
    jarName2 = "Charles"
    generate(fileNum, bookCmdCnt, otherCmdCnt)
    compare(fileNum, jarName1, jarName2)

#!/usr/bin/python3

import os
import random
import sys
import time

args = sys.argv;
if (len(args) < 2):
    print("Incorrect argument's amount");
else:
    N = int(args[1]);
    for i in range (0, N):
        pr = os.fork();
        if (pr > 0):
            if i==0:
                print("The Parent process with PID=%d" % (os.getpid()));
        else:
            random_num = int(random.uniform(5, 11));
            os.execl("./Child.py", "Child.py", str(random_num));
            break;
for i in range (0, N):
    res = os.wait();
    print("Child process with PID=%d is complete. Completion status is %d" % (res[0], res[1]));
    
#!/usr/bin/python3

import random
import os
import time
import sys

args = sys.argv;
if (len(args) < 2):
    print("Incorrect argument's amount");
else:
    sleep_time = int(args[1]);
    print("Child program with PID=%d and argument=%d is running" % (os.getpid(), sleep_time));
    time.sleep(sleep_time);
    exit_status = int(random.uniform(0, 2));
    print("The process with PID=%d completed" % (os.getpid()));
    os._exit(exit_status);
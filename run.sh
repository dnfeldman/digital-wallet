#!/usr/bin/env bash

# example of the run script for running the fraud detection algorithm with a python file,
# but could be replaced with similar files from any major language

# I'll execute my programs, with the input directory paymo_input and output the files in the directory paymo_output
#

scala -J-XX:+UseConcMarkSweepGC -classpath ./src/projectAssembly.jar digitalwallet.DigitalWallet batchPaymentSource=paymo_input/batch_payment.txt streamPaymentSource=paymo_input/stream_payment.txt feature1Output=paymo_output/output1.txt feature2Output=paymo_output/output2.txt feature3Output=paymo_output/output3.txt

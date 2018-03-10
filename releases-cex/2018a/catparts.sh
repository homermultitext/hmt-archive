#!/bin/sh

CAT=`which cat`
MV=`which mv`


for f in */*cex
do
    $CAT library.cex $f > $f+header.cex
done

$MV */*header.cex w-headers/bury



#!/bin/bash

assert() {
    eval $1
    if (( $? != 0 )) ;then
        local line=${BASH_LINENO[0]}
        local src=${BASH_SOURCE[1]}
        echo "Assertion Error at $src line $line" >&2
        echo " --> Caused by: $1" >&2
    fi
}

# assert "(( 12 < 2))"

concat() {
    local x=$1
    local y=$2
    echo $x$y
    return 0
}

# concat $A $b

eqStr() {
    local x=$1
    local y=$2

    if [ "$x" = "$y" ]; then
        return 0
    else
        return 1
    fi
}

# eqstr "you" "you"

neStr() {
    local x=$1
    local y=$2

    if [ "$x" != "$y" ]; then
        return 0
    else
        return 1
    fi
}

valueOfBool() {
    eval $1
    local ret=$?
    echo $ret
    return $ret
}

notBool() {
    if (( $1 == 0 )) ;then
        return 1
    else
        return 0
    fi
}

eqAny() {
    echo "eqAny is not support!!" >&2
    return 1   
}

neAny() {
    echo "neAny is not support!!" >&2
    return 1
}


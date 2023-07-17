# jFloatWavIO
[![Latest Release](https://gitlab.com/f-matano44/jfloatwavio/-/badges/release.svg)](https://gitlab.com/f-matano44/jfloatwavio/-/releases) <br>
Java package to read/write wav file as float or double array. <br>
It is not compatible between version 0 and version 1.


## Development environment
* [Java 17 (LTS)](https://adoptium.net/temurin/releases/?version=17)
* [ant 1.10](https://ant.apache.org/bindownload.cgi)
* [VSCode](https://code.visualstudio.com/) + [Checkstyle for Java](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle)

Part of this Library is made that using ChatGPT (GPT-4).


## Readable/writeable format
| \ |value|
|---|-----|
|Encoding|PCM_SIGNED|
|Channels|1 (monoral) or 2 (stereo)|
|Sample rate|any|
|Bit depth|8, 16, 24, 32|
|Endian|any|


## Build
Run under command, and then source codes are builded to jar file in `bin/`.
```SH
jfloatwavio/$ ant
```

## Usage
Read to `src/test/Test.java`.

## Run Test.java
```SH
jfloatwavio/$ ant
jfloatwavio/$ cd build/
jfloatwavio/build/$ java Test
```

# CREDIT
These files aren't licensed under MPL 2.0.<br>
[Please read this page.](https://zunko.jp/con_ongen_kiyaku.html)
* `src/test/zundamon.wav`: VOICEVOX:ずんだもん
* `src/test/metan.wav`: VOICEVOX:四国めたん

# COPYRIGHT
Copyright 2023 Fumiyoshi MATANO<br>

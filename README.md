# jFloatWavIO
Java package to read/write wav file as float (or double) array.

## Development environment
* [Java 17 (LTS)](https://adoptium.net/temurin/releases/?version=17)
* [ant 1.10](https://ant.apache.org/bindownload.cgi)

## Readable/writeable format
| \ |value|
|---|-----|
|Encoding|PCM_SIGNED|
|Channels|1 (monoral), 2 (stereo)|
|Sample rate|any|
|Bit depth|8, 16, 24, 32|
|Endian|any|

## Build
Run under command, and then source codes are builded to jar file in `bin/`.
```SH
jfloatwavio/$ ant
```

## Usage
Read `src/test/Test.java`.

## Run Test.java
```SH
jfloatwavio/$ ant
jfloatwavio/$ cd bin/
jfloatwavio/bin/$ java -cp "jfloatwavio.jar:" Test
```

# CREDIT
These files aren't licensed under MPL 2.0.<br>
[Please read this page.](https://zunko.jp/con_ongen_kiyaku.html)
* `src/test/zundamon.wav`: VOICEVOX:ずんだもん
* `src/test/metan.wav`: VOICEVOX:四国めたん

# COPYRIGHT
Copyright 2023 Fumiyoshi MATANO<br>

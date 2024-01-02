# jFloatWavIO
[![LGPLv3 or later](doc/images/lgplv3-88x31.png)](https://www.gnu.org/graphics/license-logos.html)
[![JitPack](https://jitpack.io/v/com.gitlab.f-matano44/jfloatwavio.svg)](https://jitpack.io/#com.gitlab.f-matano44/jfloatwavio)
[![Build status](https://gitlab.com/f-matano44/jfloatwavio/badges/main/pipeline.svg)](https://gitlab.com/f-matano44/jfloatwavio/-/jobs)

Java package to read/write wav file as double array. You can use this library under LGPLv3 or later.

* Repository & JitPack source: https://gitlab.com/f-matano44/jfloatwavio
* Mirror repository: https://github.com/f-matano44/jFloatWavIO-mirror


## Development environment
* [Gradle Kotlin DSL](https://gradle.org/) + Java 8 Compatibility Mode in Java 17
* [VSCodium](https://github.com/VSCodium/vscodium) + [Checkstyle for Java](https://github.com/jdneo/vscode-checkstyle)

Part of this Library is made that using ChatGPT.


## Readable/writeable format
| \ |value|
|---|-----|
|Encoding|PCM_SIGNED|
|Channels|1 (mono) or 2 (stereo)|
|Sample rate|any|
|Bit depth|8, 16, 24, 32|
|Endian|any|


## Usage
There is no compatibility when the major versions are different.

### Add dependency to gradle.build.kts
Please replace {version} with the desired version name (greater than 1.3.1).
```kotlin
repositories {
    maven { url = uri("https://www.jitpack.io") }
}

dependencies {
    implementation("com.gitlab.f-matano44:jfloatwavio:{version}")
}
```

### Functions
```Java
AudioFormat WavIO.getAudioFormat(File file)
double[][] WavIO.wavRead(File file)  //[0][]: mono or left, [1][]: right
void WavIO.wavWrite(String filename, int nbits, double fs, double[] mono)
void WavIO.wavWrite(String filename, int nbits, double fs, double[] left, double[] right)

double[] Converter.byte2double(byte[] byteArray, int nBits, boolean isBigEndian)
byte[] Converter.double2byte(double[] doubleArray, int nBits, boolean isBigEndian)
float[][] Converter.double2float(double[][] doubleArray)
double[][] Converter.float2double(float[][] floatArray)
```


## For developer

### Build & Publish to mavenLocal
```SH
jfloatwavio/$ ./gradlew publish
```


### Add dependency (gradle.build.kts)
```Kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("jp.f-matano44:jfloatwavio:{version}")
}
```


# COPYRIGHT
Copyright 2023 Fumiyoshi MATANO<br>

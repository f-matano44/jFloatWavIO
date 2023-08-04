# jFloatWavIO
[![Latest Release](https://gitlab.com/f-matano44/jfloatwavio/-/badges/release.svg)](https://gitlab.com/f-matano44/jfloatwavio/-/releases) [![Build status](https://gitlab.com/f-matano44/jfloatwavio/badges/main/pipeline.svg)](https://gitlab.com/f-matano44/jfloatwavio/-/jobs) [![](https://jitpack.io/v/com.gitlab.f-matano44/jfloatwavio.svg)](https://jitpack.io/#com.gitlab.f-matano44/jfloatwavio)<br>
Java package to read/write wav file as float or double array. <br>
It is not compatible between version 0 and version 1.

* Repository & JitPack source: https://gitlab.com/f-matano44/jfloatwavio
* Mirror repository: https://github.com/f-matano44/jfloatwavio


## Development environment
* [Gradle](https://gradle.org/) + [Java 17 (LTS)](https://adoptium.net/temurin/releases/?version=17)
* [VSCode](https://code.visualstudio.com/) + [Checkstyle for Java](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle)

Part of this Library is made that using ChatGPT (GPT-4).


## Readable/writeable format
| \ |value|
|---|-----|
|Encoding|PCM_SIGNED|
|Channels|1 (mono) or 2 (stereo)|
|Sample rate|any|
|Bit depth|8, 16, 24, 32|
|Endian|any|


## Usage

### Add dependency (gradle.build.kts)
```kotlin
repositories {
    ...
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    ...
    implementation("com.gitlab.f-matano44:jfloatwavio:1.3.1")
}
```

### functions
```Java
import jp.f_matano44.jfloatwavio.*;
import javax.sound.sampled.AudioFormat;

// If you use static functions...
double[][] signals = sGetSignal("path/to/inputFile.wav");
double[] left_or_mono = signal[0];
double[] right = signal[1];
AudioFormat format = sGetFormat("path/to/inputFile.wav");
int nbits = format.getSampleSizeInBits();
int fs = (int) format.getSampleRate();
sOutputData("path/to/outputFile.wav", nbits, fs, mono);
sOutputData("path/to/outputFile.wav", nbits, fs, left, right);

// else if you use WavIO object...
try {
    // input
    WavIO inWio = new WavIO("path/to/inputFile.wav");
    double[] left_or_mono = inWio.getSignal()[0];
    double[] right = inWio.getSignal()[1];
    AudioFormat format = inWio.getFormat();

    // output
    WavIO outWio = new WavIO(format, mono);
    WavIO outWio = new WavIO(foramt, left, right);
    outWio.outputData("path/to/outputFile.wav");
} catch (Exception e) {
    // Error handling
}
```


# COPYRIGHT
Copyright 2023 Fumiyoshi MATANO<br>

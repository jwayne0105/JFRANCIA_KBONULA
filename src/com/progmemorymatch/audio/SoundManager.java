package com.progmemorymatch.audio;

import com.progmemorymatch.model.GameSettings;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SoundManager {
    private static final float SAMPLE_RATE = 44_100f;

    private final ExecutorService effectsExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "sound-effects");
        thread.setDaemon(true);
        return thread;
    });

    private final AtomicBoolean backgroundRunning = new AtomicBoolean(false);
    private volatile boolean muted;
    private volatile float masterVolume = 0.65f;
    private volatile Thread backgroundThread;

    public void applySettings(GameSettings settings) {
        this.muted = settings.isMuted();
        this.masterVolume = settings.getMasterVolume();
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, masterVolume));
    }

    public boolean isMuted() {
        return muted;
    }

    public void startBackgroundMusic() {
        if (backgroundRunning.get()) {
            return;
        }
        backgroundRunning.set(true);
        backgroundThread = new Thread(this::runBackgroundLoop, "background-music");
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void stopBackgroundMusic() {
        backgroundRunning.set(false);
        Thread thread = backgroundThread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void playButtonClick() {
        playEffect(new double[]{620.0}, 70, 0.42f);
    }

    public void playMatch() {
        playEffect(new double[]{523.25, 659.25, 783.99}, 120, 0.58f);
    }

    public void playMismatch() {
        playEffect(new double[]{370.0, 277.0}, 150, 0.52f);
    }

    public void shutdown() {
        stopBackgroundMusic();
        effectsExecutor.shutdownNow();
    }

    private void runBackgroundLoop() {
        double[] melody = {220.0, 246.94, 261.63, 293.66, 329.63, 293.66, 261.63, 246.94};
        while (backgroundRunning.get()) {
            for (double note : melody) {
                if (!backgroundRunning.get()) {
                    break;
                }
                if (muted || masterVolume <= 0.001f) {
                    sleepQuietly(160);
                } else {
                    playTone(note, 150, 0.15f);
                    sleepQuietly(12);
                }
            }
        }
    }

    private void playEffect(double[] notes, int noteDurationMs, float intensity) {
        if (muted || masterVolume <= 0.001f) {
            return;
        }
        effectsExecutor.submit(() -> {
            for (double note : notes) {
                playTone(note, noteDurationMs, intensity);
                sleepQuietly(8);
            }
        });
    }

    private void playTone(double frequency, int durationMs, float intensity) {
        if (frequency <= 0) {
            sleepQuietly(durationMs);
            return;
        }

        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        int sampleCount = (int) ((durationMs / 1000.0) * SAMPLE_RATE);
        byte[] buffer = new byte[sampleCount * 2];

        float volume = Math.max(0.0f, Math.min(1.0f, masterVolume * intensity));
        for (int i = 0; i < sampleCount; i++) {
            double angle = (2.0 * Math.PI * i * frequency) / SAMPLE_RATE;
            short sample = (short) (Math.sin(angle) * Short.MAX_VALUE * volume);
            buffer[i * 2] = (byte) (sample & 0xff);
            buffer[i * 2 + 1] = (byte) ((sample >> 8) & 0xff);
        }

        SourceDataLine line = null;
        try {
            line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.stop();
        } catch (LineUnavailableException ignored) {
            // Audio output is optional. Game still runs even if system audio is unavailable.
        } finally {
            if (line != null) {
                line.close();
            }
        }
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

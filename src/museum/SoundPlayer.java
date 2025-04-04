package museum;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static SoundPlayer instance;
    private AL al;
    private Map<String, Integer> soundBuffers;
    private Map<String, Integer> soundSources;
    private Map<String, Boolean> soundLooping;
    private Map<String, float[]> soundPositions;

    private SoundPlayer() {
        try {
            ALut.alutInit();
            al = ALFactory.getAL();
            soundBuffers = new HashMap<>();
            soundSources = new HashMap<>();
            soundLooping = new HashMap<>();
            soundPositions = new HashMap<>();
        } catch (Exception e) {
            System.err.println("Error initializing OpenAL: " + e.getMessage());
        }
    }

    public static SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayer();
        }
        return instance;
    }

    public boolean loadSound(String soundName, String filePath) {
        try {
            int[] format = new int[1];
            ByteBuffer[] data = new ByteBuffer[1];
            int[] size = new int[1];
            int[] freq = new int[1];
            int[] loop = new int[1];

            ALut.alutLoadWAVFile(filePath, format, data, size, freq, loop);
            
            int[] buffer = new int[1];
            al.alGenBuffers(1, buffer, 0);
            al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
            
            int[] source = new int[1];
            al.alGenSources(1, source, 0);
            al.alSourcei(source[0], AL.AL_BUFFER, buffer[0]);
            
            soundBuffers.put(soundName, buffer[0]);
            soundSources.put(soundName, source[0]);
            soundLooping.put(soundName, false);
            soundPositions.put(soundName, new float[]{0f, 0f, 0f});
            

            try {

            	ALut.class.getMethod("alutUnloadWAVFile", int.class, ByteBuffer.class, int.class, int.class)
                    .invoke(null, format[0], data[0], size[0], freq[0]);
            } catch (Exception e) {
            	
                try {
                    ALut.class.getMethod("alutUnloadWAVFile", int.class, ByteBuffer.class, int.class, int.class)
                        .invoke(null, format[0], data[0], size[0], freq[0]);
                } catch (Exception ex) {
                    
                }
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error loading sound " + soundName + ": " + e.getMessage());
            return false;
        }
    }


    public void playSound(String soundName, boolean loop) {
        Integer source = soundSources.get(soundName);
        if (source != null) {
            al.alSourcei(source, AL.AL_LOOPING, loop ? AL.AL_TRUE : AL.AL_FALSE);
            soundLooping.put(soundName, loop);
            al.alSourcePlay(source);
        }
    }

    public void stopSound(String soundName) {
        Integer source = soundSources.get(soundName);
        if (source != null) {
            al.alSourceStop(source);
        }
    }

    public void pauseSound(String soundName) {
        Integer source = soundSources.get(soundName);
        if (source != null) {
            al.alSourcePause(source);
        }
    }

    public void setSoundVolume(String soundName, float volume) {
        Integer source = soundSources.get(soundName);
        if (source != null) {
            al.alSourcef(source, AL.AL_GAIN, Math.max(0f, Math.min(1f, volume)));
        }
    }

    public void cleanup() {
        for (int source : soundSources.values()) {
            al.alDeleteSources(1, new int[]{source}, 0);
        }
        for (int buffer : soundBuffers.values()) {
            al.alDeleteBuffers(1, new int[]{buffer}, 0);
        }
        soundBuffers.clear();
        soundSources.clear();
        soundLooping.clear();
        soundPositions.clear();
        ALut.alutExit();
    }
}
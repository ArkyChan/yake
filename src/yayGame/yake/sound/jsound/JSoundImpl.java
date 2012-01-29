/*
 * JSoundImpl.java
 * Copyright (C) 2004
 *
 * $Id: JSoundImpl.java,v 1.2 2005/12/04 17:26:55 cawe Exp $
 */
package yayGame.yake.sound.jsound;

import java.nio.ByteBuffer;

import yayGame.yake.sound.*;

/**
 * JSoundImpl
 */
public class JSoundImpl  implements Sound {
	
	static {
		S.register(new JSoundImpl());
	};

	public boolean Init() {
		SND_DMA.Init();
		if (SND_DMA.sound_started) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see jake2.sound.SoundImpl#Shutdown()
	 */
	public void Shutdown() {
		SND_DMA.Shutdown();
	}

	/* (non-Javadoc)
	 * @see jake2.sound.SoundImpl#StartSound(float[], int, int, jake2.sound.sfx_t, float, float, float)
	 */
	public void StartSound(float[] origin, int entnum, int entchannel, sfx_t sfx, float fvol, float attenuation, float timeofs) {
		SND_DMA.StartSound(origin, entnum, entchannel, sfx, fvol, attenuation, timeofs);
	}

	/* (non-Javadoc)
	 * @see jake2.sound.SoundImpl#StopAllSounds()
	 */
	public void StopAllSounds() {
		SND_DMA.StopAllSounds();
	}

	/* (non-Javadoc)
	 * @see jake2.sound.SoundImpl#Update(float[], float[], float[], float[])
	 */
	public void Update(float[] origin, float[] forward, float[] right, float[] up) {
		SND_DMA.Update(origin, forward, right, up);
	}

	/* (non-Javadoc)
	 * @see jake2.sound.Sound#getName()
	 */
	public String getName() {
		return "jsound";
	}

	/* (non-Javadoc)
	 * @see jake2.sound.Sound#BeginRegistration()
	 */
	public void BeginRegistration() {
		SND_DMA.BeginRegistration();
	}

	/* (non-Javadoc)
	 * @see jake2.sound.Sound#RegisterSound(java.lang.String)
	 */
	public sfx_t RegisterSound(String sample) {
		return SND_DMA.RegisterSound(sample);
	}

	/* (non-Javadoc)
	 * @see jake2.sound.Sound#EndRegistration()
	 */
	public void EndRegistration() {
		SND_DMA.EndRegistration();
	}

	/* (non-Javadoc)
	 * @see jake2.sound.Sound#StartLocalSound(java.lang.String)
	 */
	public void StartLocalSound(String sound) {
		SND_DMA.StartLocalSound(sound);
	}

	/* (non-Javadoc)
	 * @see jake2.sound.Sound#RawSamples(int, int, int, int, byte[])
	 */
	public void RawSamples(int samples, int rate, int width, int channels, ByteBuffer data) {
		SND_DMA.RawSamples(samples, rate, width, channels, data);
	}
    
    public void disableStreaming() {
    }

}

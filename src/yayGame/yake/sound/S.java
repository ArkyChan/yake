/*
 * S.java
 * Copyright (C) 2003
 * 
 * $Id: S.java,v 1.13 2005/12/13 00:00:25 salomo Exp $
 */
/*
Copyright (C) 1997-2001 Id Software, Inc.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package yayGame.yake.sound;

import yayGame.yake.Defines;
import yayGame.yake.game.cvar_t;
import yayGame.yake.qcommon.Com;
import yayGame.yake.qcommon.Cvar;

import java.nio.ByteBuffer;
import java.util.Vector;

/**
 * S
 */
public class S {
	
	static Sound impl;
	static cvar_t s_impl;
	
	static Vector drivers = new Vector(3);
	
	/** 
	 * Searches for and initializes all known sound drivers.
	 */
	static {	    
			// dummy driver (no sound)
			try {	    
			    Class.forName("yayGame.yake.sound.DummyDriver");
			    // initialize impl with the default value
			    // this is  necessary for dedicated mode
			    useDriver("dummy");
			} catch (Throwable e) {
			    Com.DPrintf("could not init dummy sound driver class.");
			}
			
			try {
				Class.forName("org.lwjgl.openal.AL");
				Class.forName("yayGame.yake.sound.lwjgl.LWJGLSoundImpl");
			} catch (Throwable e) {
				// ignore the lwjgl driver if runtime not in classpath
			    Com.DPrintf("could not init lwjgl sound driver class.");
			}
			
			// prefered driver
			try {
				Class.forName("net.java.games.joal.AL");
				Class.forName("yayGame.yake.sound.joal.JOALSoundImpl");
			} catch (Throwable e) {
				// ignore the joal driver if runtime not in classpath
			    Com.DPrintf("could not init joal sound driver class.");
			}
		
	};
	
	/**
	 * Registers a new Sound Implementor.
	 */
	public static void register(Sound driver) {
		if (driver == null) {
			throw new IllegalArgumentException("Sound implementation can't be null");
		}
		if (!drivers.contains(driver)) {
			drivers.add(driver);
		}
	}
	
	/**
	 * Switches to the specific sound driver.
	 */
	public static void useDriver(String driverName) {
		Sound driver = null;
		int count = drivers.size();
		for (int i = 0; i < count; i++) {
			driver = (Sound) drivers.get(i);
			if (driver.getName().equals(driverName)) {
				impl = driver;
				return;
			}
		}
		// if driver not found use dummy
		impl = (Sound)drivers.lastElement();
	}
	
	/**
	 * Initializes the sound module.
	 */
	public static void Init() {
		
		Com.Printf("\n------- sound initialization -------\n");

		cvar_t cv = Cvar.Get("s_initsound", "1", 0);
		if (cv.value == 0.0f) {
			Com.Printf("not initializing.\n");
			useDriver("dummy");
			return;			
		}

		// set the last registered driver as default
		String defaultDriver = "dummy";
		if (drivers.size() > 1){
			defaultDriver = ((Sound)drivers.lastElement()).getName();
		}
		
		s_impl = Cvar.Get("s_impl", defaultDriver, Defines.CVAR_ARCHIVE);
		useDriver(s_impl.string);

		if (impl.Init()) {
			// driver ok
			Cvar.Set("s_impl", impl.getName());
		} else {
			// fallback
			useDriver("dummy");
		}
		
		Com.Printf("\n------- use sound driver \"" + impl.getName() + "\" -------\n");
		StopAllSounds();
	}
	
	public static void Shutdown() {
		impl.Shutdown();
	}
	
	/**
	 * Called before the sounds are to be loaded and registered.
	 */
	public static void BeginRegistration() {
		impl.BeginRegistration();		
	}
	
	/**
	 * Registers and loads a sound.
	 */
	public static sfx_t RegisterSound(String sample) {
		return impl.RegisterSound(sample);
	}
	
	/**
	 * Called after all sounds are registered and loaded.
	 */
	public static void EndRegistration() {
		impl.EndRegistration();
	}
	
	/**
	 * Starts a local sound.
	 */
	public static void StartLocalSound(String sound) {
		impl.StartLocalSound(sound);		
	}
	
	/** 
	 * StartSound - Validates the parms and ques the sound up
	 * if pos is NULL, the sound will be dynamically sourced from the entity
	 * Entchannel 0 will never override a playing sound
	 */
	public static void StartSound(float[] origin, int entnum, int entchannel, sfx_t sfx, float fvol, float attenuation, float timeofs) {
		impl.StartSound(origin, entnum, entchannel, sfx, fvol, attenuation, timeofs);
	}

	/**
	 * Updates the sound renderer according to the changes in the environment,
	 * called once each time through the main loop.
	 */
	public static void Update(float[] origin, float[] forward, float[] right, float[] up) {
		impl.Update(origin, forward, right, up);
	}

	/**
	 * Cinematic streaming and voice over network.
	 */
	public static void RawSamples(int samples, int rate, int width, int channels, ByteBuffer data) {
		impl.RawSamples(samples, rate, width, channels, data);
	}
    
	/**
	 * Switches off the sound streaming.
	 */ 
    public static void disableStreaming() {
        impl.disableStreaming();
    }

	/**
	 * Stops all sounds. 
	 */
	public static void StopAllSounds() {
		impl.StopAllSounds();
	}
	
	public static String getDriverName() {
		return impl.getName();
	}
	
	/**
	 * Returns a string array containing all sound driver names.
	 */
	public static String[] getDriverNames() {
		String[] names = new String[drivers.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = ((Sound)drivers.get(i)).getName();
		}
		return names;
	}
	
	/**
	 * This is used, when resampling to this default sampling rate is activated 
	 * in the wavloader. It is placed here that sound implementors can override 
	 * this one day.
	 */
	public static int getDefaultSampleRate()
	{
		return 44100;
	}
}
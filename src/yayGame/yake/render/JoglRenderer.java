/*
 * JoglRenderer.java
 * Copyright (C) 2003
 *
 * $Id: JoglRenderer.java,v 1.10 2006/12/12 13:02:25 cawe Exp $
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
package yayGame.yake.render;

import yayGame.yake.Defines;
import yayGame.yake.client.refdef_t;
import yayGame.yake.client.refexport_t;
import yayGame.yake.qcommon.xcommand_t;
import yayGame.yake.render.opengl.JoglDriver;
import yayGame.yake.sys.JOGLKBD;
import yayGame.yake.sys.KBD;

import java.awt.Dimension;

/**
 * JoglRenderer
 * 
 * @author cwei
 */
final class JoglRenderer extends JoglDriver implements refexport_t, Ref {

	public static final String DRIVER_NAME = "jogl";
    
    	private KBD kbd = new JOGLKBD();

    	// is set from Renderer factory
    	private RenderAPI impl;

	static {
		Renderer.register(new JoglRenderer());
	};

	private JoglRenderer() {
        // singleton
	}

	// ============================================================================
	// public interface for Renderer implementations
	//
	// refexport_t (ref.h)
	// ============================================================================


    private boolean post_init = false;
    
    /** 
	 * @see yayGame.yake.client.refexport_t#Init()
	 */
	public boolean Init(int vid_xpos, int vid_ypos) {
        // init the OpenGL drivers
        impl.setGLDriver(this);
		// pre init
		if (!impl.R_Init(vid_xpos, vid_ypos)) return false;
		// calls the R_Init2() internally
		updateScreen(new xcommand_t() {
            public void execute() {
                JoglRenderer.this.post_init = impl.R_Init2();        
            }
        });
		// the result from R_Init2()
		return post_init;
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#Shutdown()
	 */
	public void Shutdown() {
		impl.R_Shutdown();
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#BeginRegistration(java.lang.String)
	 */
	public void BeginRegistration(final String map) {
		if (contextInUse) {
			impl.R_BeginRegistration(map);
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					impl.R_BeginRegistration(map);
				}
			});
		}
	}
	
	private model_t model = null;
	
	/** 
	 * @see yayGame.yake.client.refexport_t#RegisterModel(java.lang.String)
	 */
	public model_t RegisterModel(final String name) {
		if (contextInUse) {
			return impl.R_RegisterModel(name);
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					JoglRenderer.this.model = impl.R_RegisterModel(name);
				}
			});
			return model;
		}
	}

	private image_t image = null;

	/** 
	 * @see yayGame.yake.client.refexport_t#RegisterSkin(java.lang.String)
	 */
	public image_t RegisterSkin(final String name) {
		if (contextInUse) {
			return impl.R_RegisterSkin(name);
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					JoglRenderer.this.image = impl.R_RegisterSkin(name);
				}
			});
			return image;
		}
	}
	
	/** 
	 * @see yayGame.yake.client.refexport_t#RegisterPic(java.lang.String)
	 */
	public image_t RegisterPic(final String name) {
		if (contextInUse) {
			return impl.Draw_FindPic(name);
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					JoglRenderer.this.image = impl.Draw_FindPic(name);
				}
			});
			return image;
		}
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#SetSky(java.lang.String, float, float[])
	 */
	public void SetSky(final String name, final float rotate, final float[] axis) {
		if (contextInUse) {
			impl.R_SetSky(name, rotate, axis);
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					impl.R_SetSky(name, rotate, axis);
				}
			});
		}
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#EndRegistration()
	 */
	public void EndRegistration() {
		if (contextInUse) {
			impl.R_EndRegistration();
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					impl.R_EndRegistration();
				}
			});
		}
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#RenderFrame(yayGame.yake.client.refdef_t)
	 */
	public void RenderFrame(refdef_t fd) {
		impl.R_RenderFrame(fd);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawGetPicSize(java.awt.Dimension, java.lang.String)
	 */
	public void DrawGetPicSize(Dimension dim, String name) {
		impl.Draw_GetPicSize(dim, name);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawPic(int, int, java.lang.String)
	 */
	public void DrawPic(int x, int y, String name) {
		impl.Draw_Pic(x, y, name);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawStretchPic(int, int, int, int, java.lang.String)
	 */
	public void DrawStretchPic(int x, int y, int w, int h, String name) {
		impl.Draw_StretchPic(x, y, w, h, name);
	}
    
	/** 
	 * @see yayGame.yake.client.refexport_t#DrawChar(int, int, int)
	 */
	public void DrawChar(final int x, final int y, final int num) {
        if (contextInUse) {
            impl.Draw_Char(x, y, num);;
        } else {
            updateScreen(new xcommand_t() {
                public void execute() {
                    impl.Draw_Char(x, y, num);
                }
            });
        }
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawTileClear(int, int, int, int, java.lang.String)
	 */
	public void DrawTileClear(int x, int y, int w, int h, String name) {
		impl.Draw_TileClear(x, y, w, h, name);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawFill(int, int, int, int, int)
	 */
	public void DrawFill(int x, int y, int w, int h, int c) {
		impl.Draw_Fill(x, y, w, h, c);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawFadeScreen()
	 */
	public void DrawFadeScreen() {
		impl.Draw_FadeScreen();
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#DrawStretchRaw(int, int, int, int, int, int, byte[])
	 */
	public void DrawStretchRaw(int x, int y, int w, int h, int cols, int rows, byte[] data) {
		impl.Draw_StretchRaw(x, y, w, h, cols, rows, data);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#CinematicSetPalette(byte[])
	 */
	public void CinematicSetPalette(byte[] palette) {
		impl.R_SetPalette(palette);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#BeginFrame(float)
	 */
	public void BeginFrame(float camera_separation) {
		impl.R_BeginFrame(camera_separation);
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#EndFrame()
	 */
	public void EndFrame() {
		endFrame();
	}

	/** 
	 * @see yayGame.yake.client.refexport_t#AppActivate(boolean)
	 */
	public void AppActivate(boolean activate) {
		appActivate(activate);
	}

	public void screenshot() {
		if (contextInUse) {
			impl.GL_ScreenShot_f();
		} else {
			updateScreen(new xcommand_t() {
				public void execute() {
					impl.GL_ScreenShot_f();
				}
			});
		}
	}

	public int apiVersion() {
		return Defines.API_VERSION;
	}
    
	public KBD getKeyboardHandler() {
		return kbd;
	}
    	
	// ============================================================================
	// Ref interface
	// ============================================================================

	public String getName() {
		return DRIVER_NAME;
	}

	public String toString() {
		return DRIVER_NAME;
	}

	public refexport_t GetRefAPI(RenderAPI renderer) {
        	this.impl = renderer;
		return this;
	}
    
}
/*
 * JoglCommon.java
 * Copyright (C) 2004
 * 
 * $Id: JoglDriver.java,v 1.3 2006/11/22 15:05:39 cawe Exp $
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

package yayGame.yake.render.opengl;

import yayGame.yake.Defines;
import yayGame.yake.client.VID;
import yayGame.yake.qcommon.Cbuf;
import yayGame.yake.qcommon.xcommand_t;
import yayGame.yake.render.Base;
import yayGame.yake.sys.JOGLKBD;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import net.java.games.jogl.*;

/**
 * JoglCommon
 */
public abstract class JoglDriver extends JoglGL implements GLDriver, GLEventListener {
    
    protected JoglDriver() {
        // see JoglRenderer
    }

	private GraphicsDevice device;
	private DisplayMode oldDisplayMode; 
	private GLCanvas canvas;
	JFrame window;

	// window position on the screen
	int window_xpos, window_ypos;

	// handles the post initialization with JoglRenderer
	protected boolean post_init = false;
	protected boolean contextInUse = false;
	
	protected final xcommand_t INIT_CALLBACK = new xcommand_t() {
		public void execute() {
			// only used for the first run (initialization)
			// clear the screen
			glClearColor(0, 0, 0, 0);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			//
			// check the post init process
			//
			if (!post_init) {
				VID.Printf(Defines.PRINT_ALL, "Missing multi-texturing for FastJOGL renderer\n");
			}

			endFrame();
		}
	};
    
	xcommand_t callback = INIT_CALLBACK;
	
	public DisplayMode[] getModeList() {
		DisplayMode[] modes = device.getDisplayModes();
		LinkedList l = new LinkedList();
		l.add(oldDisplayMode);
		
		for (int i = 0; i < modes.length; i++) {
			DisplayMode m = modes[i];
			
			if (m.getBitDepth() != oldDisplayMode.getBitDepth()) continue;
			if (m.getRefreshRate() > oldDisplayMode.getRefreshRate()) continue;
			if (m.getHeight() < 240 || m.getWidth() < 320) continue;
			
			int j = 0;
			DisplayMode ml = null;
			for (j = 0; j < l.size(); j++) {
				ml = (DisplayMode)l.get(j);
				if (ml.getWidth() > m.getWidth()) break;
				if (ml.getWidth() == m.getWidth() && ml.getHeight() >= m.getHeight()) break;
			}
			if (j == l.size()) {
				l.addLast(m);
			} else if (ml.getWidth() > m.getWidth() || ml.getHeight() > m.getHeight()) {
				l.add(j, m);
			} else if (m.getRefreshRate() > ml.getRefreshRate()){
				l.remove(j);
				l.add(j, m);
			}
		}
		DisplayMode[] ma = new DisplayMode[l.size()];
		l.toArray(ma);
		return ma;
	}
	
	DisplayMode findDisplayMode(Dimension dim) {
		DisplayMode mode = null;
		DisplayMode m = null;
		DisplayMode[] modes = getModeList();
		int w = dim.width;
		int h = dim.height;
		
		for (int i = 0; i < modes.length; i++) {
			m = modes[i];
			if (m.getWidth() == w && m.getHeight() == h) {
				mode = m;
				break;
			}
		}
		if (mode == null) mode = oldDisplayMode;
		return mode;		
	}
		
	String getModeString(DisplayMode m) {
		StringBuffer sb = new StringBuffer();
		sb.append(m.getWidth());
		sb.append('x');
		sb.append(m.getHeight());
		sb.append('x');
		sb.append(m.getBitDepth());
		sb.append('@');
		sb.append(m.getRefreshRate());
		sb.append("Hz");
		return sb.toString();
	}

	/**
	 * @param dim
	 * @param mode
	 * @param fullscreen
	 * @return enum Base.rserr_t
	 */
	public int setMode(Dimension dim, int mode, boolean fullscreen) {

		Dimension newDim = new Dimension();

		VID.Printf(Defines.PRINT_ALL, "Initializing OpenGL display\n");

		VID.Printf(Defines.PRINT_ALL, "...setting mode " + mode + ":");
		
		/*
		 * fullscreen handling
		 */
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
       
		if (oldDisplayMode == null) {
			oldDisplayMode = device.getDisplayMode();
		}

		if (!VID.GetModeInfo(newDim, mode)) {
			VID.Printf(Defines.PRINT_ALL, " invalid mode\n");
			return Base.rserr_invalid_mode;
		}

		VID.Printf(Defines.PRINT_ALL, " " + newDim.width + " " + newDim.height + '\n');

		// destroy the existing window
		shutdown();

		window = new JFrame("yayGame.yake (jogl)");
		ImageIcon icon = new ImageIcon(getClass().getResource("/icon-small.png"));
		window.setIconImage(icon.getImage());
		
		GLCanvas canvas = GLDrawableFactory.getFactory().createGLCanvas(new GLCapabilities());

		// we want keypressed events for TAB key
		canvas.setFocusTraversalKeysEnabled(false);
		
		// TODO Use debug pipeline
		//canvas.setGL(new DebugGL(canvas.getGL()));

		canvas.setNoAutoRedrawMode(true);
		canvas.setAutoSwapBufferMode(false);
		
		canvas.addGLEventListener(this);

		window.getContentPane().add(canvas);	
		canvas.setSize(newDim.width, newDim.height);

		// register event listener
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Cbuf.ExecuteText(Defines.EXEC_APPEND, "quit");
			}
		});
		
		// D I F F E R E N T   J A K E 2   E V E N T   P R O C E S S I N G      		
		window.addComponentListener(JOGLKBD.listener);
		canvas.addKeyListener(JOGLKBD.listener);
		canvas.addMouseListener(JOGLKBD.listener);
		canvas.addMouseMotionListener(JOGLKBD.listener);
		canvas.addMouseWheelListener(JOGLKBD.listener);
				        
		if (fullscreen) {
			
			DisplayMode displayMode = findDisplayMode(newDim);
			
			newDim.width = displayMode.getWidth();
			newDim.height = displayMode.getHeight();
			window.setUndecorated(true);
			window.setResizable(false);
			
			device.setFullScreenWindow(window);
			
			if (device.isFullScreenSupported())
				device.setDisplayMode(displayMode);
			
			window.setLocation(0, 0);
			window.setSize(displayMode.getWidth(), displayMode.getHeight());
			canvas.setSize(displayMode.getWidth(), displayMode.getHeight());

			VID.Printf(Defines.PRINT_ALL, "...setting fullscreen " + getModeString(displayMode) + '\n');

		} else {
			window.setLocation(window_xpos, window_ypos);
			window.pack();
			window.setResizable(false);
			window.setVisible(true);
		}
		
		while (!canvas.isDisplayable()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
		canvas.requestFocus();
		
		this.canvas = canvas;

		Base.setVid(newDim.width, newDim.height);

		// let the sound and input subsystems know about the new window
		VID.NewWindow(newDim.width, newDim.height);

		return Base.rserr_ok;
	}

	public void shutdown() {
		if (oldDisplayMode != null && device.getFullScreenWindow() != null) {
			try {
				if (device.isFullScreenSupported())
					device.setDisplayMode(oldDisplayMode);
				device.setFullScreenWindow(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (window != null) {
		    // this is very important to change the GL context
		    if (canvas != null) {
		        canvas.setVisible(false);
		        window.remove(canvas);
		        canvas = null;
		    }
			window.dispose();
		}
		post_init = false;
		callback = INIT_CALLBACK;
	}

	/**
	 * @return true
	 */
	public boolean init(int xpos, int ypos) {
		// do nothing
		window_xpos = xpos;
		window_ypos = ypos;
		return true;
	}

    public void beginFrame(float camera_separation) {
        // do nothing
    }

    public void endFrame() {
		glFlush();
		canvas.swapBuffers();
	}
    
    public void appActivate(boolean activate) {
        // do nothing
    }

    public void enableLogging(boolean enable) {
        // do nothing
    }

    public void logNewFrame() {
        // do nothing
    }

    /* 
	 * @see yayGame.yake.client.refexport_t#updateScreen()
	 */
	public void updateScreen() {
		this.callback = INIT_CALLBACK;
		canvas.display();
	}
	
	public void updateScreen(xcommand_t callback) {
		this.callback = callback;
		canvas.display();
	}	
	// ============================================================================
	// GLEventListener interface
	// ============================================================================

	/* 
	* @see net.java.games.jogl.GLEventListener#init(net.java.games.jogl.GLDrawable)
	*/
	public void init(GLDrawable drawable) {
		setGL(drawable.getGL());
	}

	/* 
	 * @see net.java.games.jogl.GLEventListener#display(net.java.games.jogl.GLDrawable)
	 */
	public void display(GLDrawable drawable) {
        setGL(drawable.getGL());
		
		contextInUse = true;
		callback.execute();
		contextInUse = false;
	}

	/* 
	* @see net.java.games.jogl.GLEventListener#displayChanged(net.java.games.jogl.GLDrawable, boolean, boolean)
	*/
	public void displayChanged(GLDrawable drawable, boolean arg1, boolean arg2) {
		// do nothing
	}

	/* 
	* @see net.java.games.jogl.GLEventListener#reshape(net.java.games.jogl.GLDrawable, int, int, int, int)
	*/
	public void reshape(GLDrawable drawable, int x, int y, int width, int height) {
		// do nothing
	}

}

/* 
 * Copyleft (o) 2012 James Baiera
 * All wrongs reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.stonetolb;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.stonetolb.game.module.Module;
import com.stonetolb.game.module.WorldModule;
import com.stonetolb.render.Camera;

/**
 * This is our hook for launching the game, and also the home
 * of the main game loop. From here, we'll be running the frame
 * render operation, which will draw each sprite contained in the 
 * list of things to draw.
 * 
 * @author comet
 */
public class Game {

	/** The normal title of the window */
	private String WINDOW_TITLE	= "Stonetolb 0.0.4";

	/** The width of the game display area */
	private int	width = 800;

	/** The height of the game display area */
	private int	height = 600;

	/** The time at which the last rendering loop started from the point of view of the game logic */
	private long lastLoopTime = getTime();

	/** The time since the last record of fps */
	private long lastFpsTime;

	/** The recorded fps */
	private int	fps;
	private static long	timerTicksPerSecond	= Sys.getTimerResolution();

	/** True if the game is currently "running", i.e. the game loop is looping */
	public static boolean gameRunning = true;

	/** Whether we're running in fullscreen mode */
	private boolean	fullscreen;

	/** Is this an application or applet */
	private static boolean isApplication;

	/** Game Module that handles the game play */
	Module world = new WorldModule();
	
	/**
	 * Construct our game and set it running.
	 * @param fullscreen
	 *
	 */
	public Game(boolean fullscreen) {
		this.fullscreen = fullscreen;
		initialize();
	}

	/**
	 * Get the high resolution time in milliseconds
	 *
	 * @return The high resolution time in milliseconds
	 */
	public static long getTime() {
		// we get the "timer ticks" from the high resolution timer
		// multiply by 1000 so our end result is in milliseconds
		// then divide by the number of ticks in a second giving
		// us a nice clear time in milliseconds
		return (Sys.getTime() * 1000) / timerTicksPerSecond;
	}

	/**
	 * Sleep for a fixed number of milliseconds.
	 *
	 * @param duration The amount of time in milliseconds to sleep for
	 */
	public static void sleep(long duration) {
		try {
			Thread.sleep((duration * timerTicksPerSecond) / 1000);
		} catch (InterruptedException inte) {
		}
	}

	/**
	 * Initialize the common elements for the game
	 */
	public void initialize() {
		// initialize the window beforehand
		try {
			setDisplayMode();
			Display.setTitle(WINDOW_TITLE);
			Display.setFullscreen(fullscreen);
			Display.create();

			// grab the mouse, dont want that hideous cursor when we're playing!
			//if (isApplication) {
			//	Mouse.setGrabbed(true);
			//}

			// enable textures since we're going to use these for our sprites
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glEnable(GL_DEPTH_TEST);
			
			// Testing some states
			GL11.glClearDepth(1.0);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			glEnable(GL11.GL_ALPHA_TEST);
			glEnable(GL11.GL_CULL_FACE);
			
			// Create the Camera and update it's position on the plane
			Camera.createCamera(width, height);
			Camera.getCamera().moveCamera(); // Update Camera to move to position
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glViewport(0, 0, width, height);
			
		} catch (LWJGLException le) {
			System.out.println("Game exiting - exception in initialization:");
			le.printStackTrace();
			Game.gameRunning = false;
			return;
		}

		// setup the initial game state
		startGame();
	}

	/**
	* Sets the display mode for fullscreen mode
	*/
	private boolean setDisplayMode() {
		try {
			// We get the Display Modes
			// Let's just ignore this for a bit...
			//DisplayMode[] displayModes = org.lwjgl.opengl.Display.getAvailableDisplayModes();
			
			//for(DisplayMode displayMode : displayModes) {
			//	System.out.println(displayMode.toString());
			//}
			
			//If full screen, set's full screen
			if (fullscreen)
			{
				Display.setFullscreen(fullscreen);
			}
			else
			{
				//Otherwise just run in window
				DisplayMode dm = new DisplayMode(width,height);
		    	Display.setDisplayMode(dm);
			}
				
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to enter fullscreen, continuing in windowed mode");
		}
		
		return false;
	}

	/**
	 * Initializes the game stack and readies it for the game loop
	 */
	private void startGame() {
		// Create the first game module and init
		world.init();
	}

	/**
	 * Run the main game loop. This method keeps rendering the scene
	 * and requesting that the callback updates its screen.
	 */
	private void gameLoop() {
		while (Game.gameRunning) {
		
			// clear screen
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
			// let subsystem paint
			frameRendering();

			// update window contents
			Display.update();
		}

		// clean up
		Display.destroy();
	}

	/**
	 * Notification that a frame is being rendered. Responsible for
	 * running game logic and rendering the scene.
	 */
	public void frameRendering() {
		//SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
		Display.sync(60);

		// work out how long its been since the last update, this
		// will be used to calculate how far the entities should
		// move this loop
		long delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		lastFpsTime += delta;
		fps++;

		// update our FPS counter if a second has passed
		if (lastFpsTime >= 1000) {
			Display.setTitle(WINDOW_TITLE + " (FPS: " + fps + ")");
			lastFpsTime = 0;
			fps = 0;
		}

		// Allow Game Logic
		world.step(delta);
					
		// render the game frame
		world.render(delta);
		
		// Update Camera's position
		Camera.getCamera().moveCamera();
		
		// if escape has been pressed, stop the game
		if ((Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) && isApplication) {
			Game.gameRunning = false;
		}
	}

	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 *
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
		isApplication = true;
		System.out.println("Use -fullscreen for fullscreen mode");
		new Game((argv.length > 0 && "-fullscreen".equalsIgnoreCase(argv[0]))).execute();
		System.exit(0);
	}

	/**
	 * Starts game loop, which runs until signaled to stop
	 */
	public void execute() {
		gameLoop();
	}
}
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

package com.stonetolb.render;

import org.lwjgl.opengl.GL11;

import com.stonetolb.engine.Entity;
import com.stonetolb.util.Pair;

/**
 * The Camera is a special class that controls where the player is looking at the moment.
 * <p>
 * Camera is a singleton since there can only ever be one camera at a time. The Camera can
 * be parented to an {@link Entity} object to control it's location. Normally, one would not 
 * directly set the coordinates of the camera, but instead attach the camera to an entity and
 * allow the entity to control the movement.
 * 
 * @author james.baiera
 *
 */
public class Camera {
	
	private static Camera INSTANCE = null;
	private static Pair<Float, Float> ORIGIN = new Pair<Float, Float>(0F, 0F);
	
	private Pair<Float, Float> position;
	private int screenWidth;
	private int screenHeight;
	private Entity parent;
	
	public static synchronized void createCamera(int pWidth, int pHeight) {
		if (INSTANCE == null) {
			INSTANCE = new Camera(pWidth, pHeight);
		}
	}
	
	public static Camera getCamera() {
		return INSTANCE;
	}
	
	private Camera(int pWidth, int pHeight) {
		screenWidth = pWidth;
		screenHeight = pHeight;
		position = ORIGIN;
		parent = null;
	}
	
	public void setParent(Entity pParent) {
		parent = pParent;
		updatePosition();
	}
	
	private void updatePosition() {
		if (parent != null) {
			position.x = parent.getAbsolute().x - ((float)screenWidth / 2.0F);
			position.y = parent.getAbsolute().y - ((float)screenHeight / 2.0F);
		}
		else {
			position = ORIGIN;
		}
	}
	
	public void moveCamera() {
		updatePosition();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(
				  position.x.doubleValue()
				, position.x.doubleValue() + (double)screenWidth
				, position.y.doubleValue() + (double)screenHeight
				, position.y.doubleValue()
				, screenHeight
				, screenHeight * -1
			);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}

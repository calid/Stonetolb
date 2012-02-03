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

package STEngine;

import static org.lwjgl.opengl.GL11.*;

/**
 * A texture to be bound within OpenGL. This object is responsible for
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 * 
 * Since textures need to be powers of 2, the actual texture may be
 * considerably enlarged than the source image and hence the texture
 * mapping coordinates need to be adjusted to match up the drawing the
 * sprite against the texture.
 * 
 * @author comet
 */
public class Texture {

	/** The GL target type */
	private int		target;

	/** The GL texture ID */
	private int		textureID;

	/** The height of the image */
	private int		height;

	/** The width of the image */
	private int		width;

	/** The width of the texture */
	private int		texWidth;

	/** The height of the texture */
	private int		texHeight;

	/** The ratio of the width of the image to the texture */
	private float	widthRatio;

	/** The ratio of the height of the image to the texture */
	private float	heightRatio;

	/**
	 * Create a new texture
	 *
	 * @param target The GL target
	 * @param textureID The GL texture ID
	 */
	public Texture(int target, int textureID) {
		this.target = target;
		this.textureID = textureID;
	}

	/**
	 * Bind the specified GL context to a texture
	 */
	public void bind() {
		glBindTexture(target, textureID);
	}

	/**
	 * Set the height of the image
	 *
	 * @param height The height of the image
	 */
	public void setHeight(int height) {
		this.height = height;
		setHeight();
	}

	/**
	 * Set the width of the image
	 *
	 * @param width The width of the image
	 */
	public void setWidth(int width) {
		this.width = width;
		setWidth();
	}

	/**
	 * Get the height of the original image
	 *
	 * @return The height of the original image
	 */
	public int getImageHeight() {
		return height;
	}

	/**
	 * Get the width of the original image
	 *
	 * @return The width of the original image
	 */
	public int getImageWidth() {
		return width;
	}

	/**
	 * Get the height of the physical texture
	 *
	 * @return The height of physical texture
	 */
	public float getHeight() {
		return heightRatio;
	}

	/**
	 * Get the width of the physical texture
	 *
	 * @return The width of physical texture
	 */
	public float getWidth() {
		return widthRatio;
	}

	/**
	 * Set the height of this texture
	 *
	 * @param texHeight The height of the texture
	 */
	public void setTextureHeight(int texHeight) {
		this.texHeight = texHeight;
		setHeight();
	}

	/**
	 * Set the width of this texture
	 *
	 * @param texWidth The width of the texture
	 */
	public void setTextureWidth(int texWidth) {
		this.texWidth = texWidth;
		setWidth();
	}

	/**
	 * Set the height of the texture. This will update the
	 * ratio also.
	 */
	private void setHeight() {
		if (texHeight != 0) {
			heightRatio = ((float) height) / texHeight;
		}
	}

	/**
	 * Set the width of the texture. This will update the
	 * ratio also.
	 */
	private void setWidth() {
		if (texWidth != 0) {
			widthRatio = ((float) width) / texWidth;
		}
	}
}
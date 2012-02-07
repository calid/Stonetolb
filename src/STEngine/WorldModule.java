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

public class WorldModule extends Module {
	private Animation 		test;
	private Entity			test2;
	private int				testx;
	private int 			testy;
	
	@Override
	public void init() {
		test = new Animation(600);
		test.addFrame(new Sprite("test.gif"));
		test.addFrame(new Sprite("test2.gif"));
		test.addFrame(new Sprite("test.gif"));
		test.addFrame(new Sprite("test3.gif"));
		
		test2 = new Entity(70, 190);
		test2.addAnimation("wiggle",test);
		test2.setHorizontalMovement(75);
		
		testx = 200;
		testy = 200;
	}

	@Override
	public void step() {
		if (test2.getX() < 100) {
			test2.setHorizontalMovement(75);
		} else if (test2.getX() > 300) {
			test2.setHorizontalMovement(-75);
		}
	}

	@Override
	public void render(long delta) {
		test.draw(testx, testy, delta);
		test2.move(delta);
		test2.draw();
	}
}

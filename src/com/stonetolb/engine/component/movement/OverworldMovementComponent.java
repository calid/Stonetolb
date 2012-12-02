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

package com.stonetolb.engine.component.movement;

import com.stonetolb.engine.component.EntityComponent;
import com.stonetolb.engine.profiles.WorldProfile.WorldDirection;

/**
 * OverworldMovementComponent takes the Entity's state and uses it to
 * extrapolate where to set the entity's new position in the world
 * 
 * @author james.baiera
 *
 */
public class OverworldMovementComponent extends EntityComponent {
	
	public OverworldMovementComponent(String pId) {
		id = pId;
	}
	
	@Override
	public void update(long delta) {
		WorldDirection dir;
		if(parent.getDirection() == WorldDirection.UP.getDirection()) {
			dir = WorldDirection.UP;
		} else if (parent.getDirection() == WorldDirection.RIGHT.getDirection()) {
			dir = WorldDirection.RIGHT;
		} else if (parent.getDirection() == WorldDirection.DOWN.getDirection()) {
			dir = WorldDirection.DOWN;
		} else if (parent.getDirection() == WorldDirection.LEFT.getDirection()) {
			dir = WorldDirection.LEFT;
		} else {
			dir = WorldDirection.STILL;
		}
		
		parent.getPosition().x += (((float)((delta * (parent.getSpeed())) / 1000)) * dir.getXFactor());
		parent.getPosition().y += (((float)((delta * (parent.getSpeed())) / 1000)) * dir.getYFactor());
	}

}

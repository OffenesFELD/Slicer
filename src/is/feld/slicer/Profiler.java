/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 *
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package is.feld.slicer;

import java.util.Hashtable;

public class Profiler {

    private static Hashtable<String, Profile> _myProfiles = new Hashtable<String, Profile>();

    public static void start(final String theIdent){
        if(_myProfiles.containsKey(theIdent)) {
            _myProfiles.remove(theIdent);
        }

        _myProfiles.put(theIdent, new Profile());


        System.out.println("Profiler > Started " + theIdent);
    }


    public static void stop(final String theIdent) {
        if(_myProfiles.containsKey(theIdent)) {
            final Profile myProfile = _myProfiles.get(theIdent);
            myProfile.stop();

            System.out.println("Profiler < " + theIdent + " took " + myProfile.result() + " ms");
        }
    }


    private static class Profile {
        private long _myStartTime;
        private long _myEndTime;

        public Profile() {
            _myStartTime = System.nanoTime();
        }

        private void stop() {
            _myEndTime = System.nanoTime();
        }

        private float result() {
            return (_myEndTime - _myStartTime)/1000000.0f;
        }
    }
}

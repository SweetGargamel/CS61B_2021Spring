#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'test34-merge-conflicts.in'.
#
# Test Description: Create two branches and merge 'other' into 'master'
#                   with a merge conflict.
#
# NOTE: This script uses `echo` to create files with the specific content
#       provided in the test case, which is required to cause the conflict.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
# To run it all at once, comment out the 'trap' line below.


# === From prelude1.inc, setup1.inc, and setup2.inc ===
java gitlet.Main init
echo "This is a wug." > f.txt
echo "This is not a wug." > g.txt
java gitlet.Main add g.txt
java gitlet.Main add f.txt
java gitlet.Main commit "Two files"

# === From test34-merge-conflicts.in ===

# 1. Create the 'other' branch
java gitlet.Main branch other

# 2. Make commits on the 'master' branch
echo "Another wug." > h.txt
java gitlet.Main add h.txt
java gitlet.Main rm g.txt
echo "Another wug." > f.txt
java gitlet.Main add f.txt
java gitlet.Main commit "Add h.txt, remove g.txt, and change f.txt"

# 3. Make conflicting commits on the 'other' branch
java gitlet.Main checkout other
echo "This is not a wug." > f.txt
java gitlet.Main add f.txt
echo "And yet another wug." > k.txt
java gitlet.Main add k.txt
java gitlet.Main commit "Add k.txt and modify f.txt"

# 4. Switch back to master and attempt the merge
java gitlet.Main checkout master
java gitlet.Main log
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

java gitlet.Main merge other
trap - DEBUG

# 5. Check the state after the failed merge
java gitlet.Main log
java gitlet.Main status


# Clean up the trap to exit cleanly
# Clean up the trap to exit cleanly
rm *.txt
rm -f -r .gitlet
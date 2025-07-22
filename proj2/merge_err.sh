#!/bin/bash

# ==============================================================================
# This is an auto-generated script from 'test36-merge-err.in'.
#
# Test Description: Checks for various merge error cases. This includes trying
# to merge a branch with itself, merging a non-existent branch, and merging
# when the working directory is not clean.
# ==============================================================================

# To run this script step-by-step, press Enter at each prompt.
# To run it all at once, comment out the 'trap' line below.

# === From prelude1.inc, setup1.inc, and setup2.inc ===

java gitlet.Main init
echo "wug.txt" > f.txt
echo "notwug.txt" > g.txt
java gitlet.Main add g.txt
java gitlet.Main add f.txt
java gitlet.Main commit "Two files"

# === From test36-merge-err.in ===

# 1. Create and modify a new branch
java gitlet.Main branch other
echo "wug2.txt" > h.txt
java gitlet.Main add h.txt
java gitlet.Main rm g.txt
java gitlet.Main commit "Add h.txt and remove g.txt"

# 2. Test Error: Cannot merge a branch with itself.
java gitlet.Main checkout other
java gitlet.Main merge other

# 3. Continue setup on 'other' branch
java gitlet.Main rm f.txt
echo "wug3.txt" > k.txt
java gitlet.Main add k.txt
java gitlet.Main commit "Add k.txt and remove f.txt"
java gitlet.Main checkout master

# 4. Test Error: Merging a branch that does not exist.
java gitlet.Main merge foobar

# 5. Test Error: Merging with an untracked file in the way.
echo "wug.txt" > k.txt
java gitlet.Main merge other
rm k.txt
java gitlet.Main status
# 6. Test Error: Merging with uncommitted (staged) changes.
echo "wug.txt" > k.txt
java gitlet.Main add k.txt
java gitlet.Main status
trap 'read -p "EXECUTE: [$BASH_COMMAND] - Press Enter..."' DEBUG

java gitlet.Main merge other

# 7. Cleanup
java gitlet.Main rm k.txt
rm k.txt


# Clean up the trap to exit cleanly
trap - DEBUG
# Clean up the trap to exit cleanly
rm *.txt
rm -f -r .gitlet
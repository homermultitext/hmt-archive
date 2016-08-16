
echo "Extracting list of inventoried scholia..."
cut -f2 -d, citebldr/build/archive/collections/scholiaInventory.csv | sort > inventoried.txt

echo "Extracting list of edited scholia..."

cut -f1 -d" " graphs/build/ttl/cts.ttl | grep 5026 | grep -v ater | perl -pe 's/[<>]//g' - | sort > edited.txt

echo "Diffing..."

diff edited.txt inventoried.txt > guilty.txt


echo "In guilty.txt, '>' means 'inventoried, not edited'"
echo "'<' means 'edited, not inventoried'"

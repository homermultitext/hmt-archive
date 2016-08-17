
echo "Extracting list of inventoried scholia..."
cut -f2 -d, citebldr/build/archive/collections/scholiaInventory.csv | sort > inventoried.txt

echo "Extracting list of edited scholia..."

grep Content graphs/build/ttl/cts.ttl | cut -f1 -d" " -  |  grep 5026 | grep -v ater | perl -pe 's/[<>]//g' - | sort | uniq > edited.txt

echo "Diffing..."

diff edited.txt inventoried.txt > guilty.txt


echo "In guilty.txt, '>' means 'inventoried, not edited'"
echo "'<' means 'edited, not inventoried'"

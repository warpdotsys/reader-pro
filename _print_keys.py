import csv
from collections import Counter

csv_path = r"C:\Users\chong\reader-pro-3.2.14-reverse\BEST_OF_3_SELECTION.csv"
keys = [
    "BookController",
    "UserController",
    "YueduApi",
    "BookChapterList",
    "Coroutine",
    "BookHelp",
    "BookContent",
    "BaseController",
    "AnalyzeRule",
    "LicenseController",
    "RestVerticle",
]
wins = Counter()
rows = list(csv.DictReader(open(csv_path, encoding="utf-8")))
for row in rows:
    wins[row["winner"]] += 1
print("Winners:", dict(wins))
print()
for row in rows:
    b = row["base"]
    if b.endswith("Kt"):
        continue
    if any(k in b for k in keys):
        print(
            f"{row['winner']:4s} hard={row['winner_hard']:>2} struct={row['winner_struct']:>4} "
            f"bc={row['winner_bytecode']:>5} L={row['winner_lines']:>6}  {b}"
        )

# still imperfect
print("\nImperfect winners (hard>0 or struct>5 or bc>100):")
n = 0
for row in rows:
    h, st, bc = int(row["winner_hard"] or 0), int(float(row["winner_struct"] or 0)), int(float(row["winner_bytecode"] or 0))
    if h or st > 5 or bc > 100:
        n += 1
        print(f"  {row['winner']:4s} h={h} st={st} bc={bc}  {row['base']}")
print("total imperfect:", n)

import csv


def read_csv(filepath):
    rows = list()
    with open(filepath, 'r') as file:
        csvreader = csv.reader(file)
        for row in csvreader:
            rows.append(row)
    return rows


def write_csv(filepath, rows):
    with open(filepath, 'w', newline='') as file:
        csvwriter = csv.writer(file)
        for row in rows:
            csvwriter.writerow(row)

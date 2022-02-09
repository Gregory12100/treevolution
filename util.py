import csv


def read_csv(filepath):
    rows = list()
    with open(filepath, 'r') as file:
        csvreader = csv.reader(file)
        for row in csvreader:
            rows.append(row)
    return rows

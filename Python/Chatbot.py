import pandas as pd
import math
import re
import sys
from collections import defaultdict
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize

# Constants
STOPWORDS = set(stopwords.words("english"))  # Set of common English stopwords
DATASET = r"C:\Users\Lenovo\Desktop\chat\pitch_decks_dataset.csv"  # Path to the dataset

# Initialize structures
document_filenames = {}  # Dictionary to store document filenames
N = 0  # Total number of documents
vocabulary = set()  # Set to store unique terms
postings = defaultdict(dict)  # Dictionary to store term postings (term frequencies in documents)
document_frequency = defaultdict(int)  # Dictionary to store document frequencies of terms
length = defaultdict(float)  # Dictionary to store lengths of document vectors

def main():
    # Get the corpus from the dataset
    get_corpus()

    # Initialize terms and postings for the corpus
    initialize_terms_and_postings()

    # Set document frequencies for all terms
    initialize_document_frequencies()

    # Set document vector lengths
    initialize_lengths()

    # Allow for search
    while True:
        results = do_search()
        if results:
            print("List of projects")
            for company in results[:5]:  # Retrieve top 5 results
                print(company)
        else:
            print("No projects found related to your question")

def get_corpus():
    global df, N
    # Load the dataset
    df = pd.read_csv(DATASET, encoding='ISO-8859-1')
    N = len(df)  # Set the total number of documents

def initialize_terms_and_postings():
    global vocabulary, postings
    for index, row in df.iterrows():
        document = row['Full Description']
        if not isinstance(document, str):
            continue  # Skip if not a string
        document = remove_special_characters(document)  # Remove special characters from the document
        document = remove_digits(document)  # Remove digits from the document
        terms = tokenize(document)  # Tokenize the document
        unique_terms = set(terms)  # Get unique terms from the document
        vocabulary = vocabulary.union(unique_terms)  # Add unique terms to the vocabulary
        for term in unique_terms:
            postings[term][index] = terms.count(term)  # Update term postings with term frequencies

def combine_fields(row):
    combined = ""
    for field in ['Company Name', 'Slogan', 'Amount Raised', 'Year', 'Stage', 'Business Model',
                  'Full Description', 'Investors', 'About', 'Industry', 'Tags', 'Customer Model',
                  'Website', 'Legal Name', 'Type']:
        if isinstance(row[field], str):
            combined += row[field] + " "
    return combined.strip()

def tokenize(document):
    terms = word_tokenize(document)  # Tokenize the document
    terms = [term.lower() for term in terms if term.lower() not in STOPWORDS]  # Remove stopwords and convert to lowercase
    return terms

def initialize_document_frequencies():
    global document_frequency
    for term in vocabulary:
        document_frequency[term] = len(postings[term])  # Set document frequencies for all terms

def initialize_lengths():
    global length
    for index in df.index:
        l = 0
        for term in vocabulary:
            l += term_frequency(term, index) ** 2  # Calculate the squared term frequencies
        length[index] = math.sqrt(l)  # Set the length of the document vector
    for index in df.index:
        if length[index] == 0:
            length[index] = 1.0  # Avoid zero length

def term_frequency(term, index):
    if index in postings[term]:
        return postings[term][index]  # Return the term frequency in the document
    else:
        return 0.0

def inverse_document_frequency(term):
    if term in vocabulary:
        return math.log(N / document_frequency[term], 2)  # Calculate the inverse document frequency
    else:
        return 0.0

def do_search():
    query = tokenize(input("Search projects"))  # Tokenize the search query
    if query == []:
        sys.exit()
    scores = sorted(
        [(index, similarity(query, index)) for index in df.index],
        key=lambda x: x[1],
        reverse=True,
    )
    # Filter results with similarity >= 95%
    threshold = 0.95
    results = [df.loc[index, 'Company Name'] for index, score in scores if score >= threshold]
    return results

def similarity(query, index):
    similarity = 0.0
    for term in query:
        if term in vocabulary:
            similarity += term_frequency(term, index) * inverse_document_frequency(term)  # Calculate the weighted term frequency
    similarity = similarity / length[index]  # Normalize by the length of the document vector
    return similarity

def remove_special_characters(text):
    regex = re.compile(r"[^a-zA-Z0-9\s]")
    return re.sub(regex, "", text)  # Remove special characters from the text

def remove_digits(text):
    regex = re.compile(r"\d")
    return re.sub(regex, "", text)  # Remove digits from the text

if __name__ == "__main__":
    main()

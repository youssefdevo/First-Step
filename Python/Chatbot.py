from flask import Flask, request, jsonify  # Importing necessary modules from Flask
import pandas as pd  # Importing pandas for data manipulation
import math  # Importing math for mathematical operations
import re  # Importing re for regular expressions
from collections import defaultdict  # Importing defaultdict to handle default values in dictionaries
from nltk.corpus import stopwords  # Importing stopwords from NLTK
from nltk.tokenize import word_tokenize  # Importing word_tokenize from NLTK for tokenizing text
from flask_cors import CORS  # Importing CORS for Cross-Origin Resource Sharing

app = Flask(__name__)  # Initializing the Flask application
CORS(app)  # Enable CORS for all routes

STOPWORDS = set(stopwords.words("english"))  # Defining a set of stopwords
DATASET = r"C:\Users\Lenovo\Desktop\chat\pitch_decks_dataset.csv"  # Path to the dataset

document_filenames = {}  # Dictionary to store document filenames
N = 0  # Total number of documents
vocabulary = set()  # Set to store the vocabulary
postings = defaultdict(dict)  # Dictionary to store postings (term -> document frequency)
document_frequency = defaultdict(int)  # Dictionary to store document frequencies (term -> document frequency)
length = defaultdict(float)  # Dictionary to store document lengths

# Function to initialize the system
def initialize():
    get_corpus()  # Load the dataset
    initialize_terms_and_postings()  # Initialize terms and postings
    initialize_document_frequencies()  # Initialize document frequencies
    initialize_lengths()  # Initialize document lengths

# Function to load the dataset
def get_corpus():
    global df, N
    df = pd.read_csv(DATASET, encoding='ISO-8859-1')  # Read the dataset
    N = len(df)  # Set the total number of documents

# Function to initialize terms and postings
def initialize_terms_and_postings():
    global vocabulary, postings
    for index, row in df.iterrows():  # Iterate through each row in the dataset
        document = row['Full Description']  # Get the document (full description)
        if not isinstance(document, str):
            continue
        document = remove_special_characters(document)  # Remove special characters
        document = remove_digits(document)  # Remove digits
        terms = tokenize(document)  # Tokenize the document
        unique_terms = set(terms)  # Get unique terms
        vocabulary = vocabulary.union(unique_terms)  # Add terms to the vocabulary
        for term in unique_terms:  # For each unique term
            postings[term][index] = terms.count(term)  # Update postings with term frequency

# Function to combine multiple fields into a single string
def combine_fields(row):
    combined = ""
    for field in ['Company Name', 'Slogan', 'Amount Raised', 'Year', 'Stage', 'Business Model',
                  'Full Description', 'Investors', 'About', 'Industry', 'Tags', 'Customer Model',
                  'Website', 'Legal Name', 'Type']:  # List of fields to combine
        if isinstance(row[field], str):
            combined += row[field] + " "
    return combined.strip()

# Function to tokenize a document
def tokenize(document):
    terms = word_tokenize(document)  # Tokenize the document
    terms = [term.lower() for term in terms if term.lower() not in STOPWORDS]  # Remove stopwords
    return terms

# Function to initialize document frequencies
def initialize_document_frequencies():
    global document_frequency
    for term in vocabulary:  # For each term in the vocabulary
        document_frequency[term] = len(postings[term])  # Set document frequency

# Function to initialize document lengths
def initialize_lengths():
    global length
    for index in df.index:  # For each document
        l = 0
        for term in vocabulary:  # For each term in the vocabulary
            l += term_frequency(term, index) ** 2  # Sum of squared term frequencies
        length[index] = math.sqrt(l)  # Set the document length
    for index in df.index:
        if length[index] == 0:
            length[index] = 1.0  # Ensure no document has length zero

# Function to get term frequency
def term_frequency(term, index):
    if index in postings[term]:
        return postings[term][index]
    else:
        return 0.0

# Function to get inverse document frequency
def inverse_document_frequency(term):
    if term in vocabulary:
        return math.log(N / document_frequency[term], 2)
    else:
        return 0.0

# Function to search for documents matching the query
def do_search(query):
    query = tokenize(query)  # Tokenize the query
    scores = sorted(
        [(index, similarity(query, index)) for index in df.index],
        key=lambda x: x[1],
        reverse=True,
    )  # Compute similarity scores and sort them
    threshold = 0.95  # Define a threshold for similarity
    results = [df.loc[index, 'Company Name'] for index, score in scores if score >= threshold]
    return results[:5]  # Return only the top 5 results

# Function to compute similarity between query and document
def similarity(query, index):
    similarity = 0.0
    for term in query:
        if term in vocabulary:
            similarity += term_frequency(term, index) * inverse_document_frequency(term)
    similarity = similarity / length[index]
    return similarity

# Function to remove special characters from text
def remove_special_characters(text):
    regex = re.compile(r"[^a-zA-Z0-9\s]")
    return re.sub(regex, "", text)

# Function to remove digits from text
def remove_digits(text):
    regex = re.compile(r"\d")
    return re.sub(regex, "", text)

# Define the /search route
@app.route('/search', methods=['GET'])
def search():
    query = request.args.get('query')
    if not query:
        return jsonify({'error': 'Query parameter is missing'}), 400

    results = do_search(query)  # Perform search
    return jsonify({'results': results})  # Return results as JSON

if __name__ == "__main__":
    initialize()  # Initialize the system
    app.run(host='0.0.0.0', port=5000)  # Run the Flask application

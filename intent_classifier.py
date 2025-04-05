from transformers import pipeline

# Define your custom label mapping
label_map = {0: "EVENT", 1: "COMPLAINT", 2: "FEEDBACK", 3: "UNKNOWN"}

# Load your fine-tuned model
nlp_model = pipeline("text-classification", model="./fine_tuned_model", tokenizer="./fine_tuned_model")

def classify_intent(user_input):
    result = nlp_model(user_input)
    predicted_label = int(result[0]['label'].split("_")[-1])  # Extract the number from "LABEL_X"
    return label_map[predicted_label]  # Map back to custom label

# Example usage
if __name__ == "__main__":
    print(classify_intent("which artist is performinf on srijan day 2"))  
    print(classify_intent("complaint"))  
    print(classify_intent("feedback")) 
    print(classify_intent("what is your name")) 
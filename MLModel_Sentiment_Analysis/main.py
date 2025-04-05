from fastapi import FastAPI
from pydantic import BaseModel
import torch
from transformers import AutoModelForSequenceClassification, AutoTokenizer

app = FastAPI()

# Define request body structure
class InputText(BaseModel):
    text: str

# Load a better sentiment model (3-class: positive, neutral, negative) jadklf
MODEL_NAME = "cardiffnlp/twitter-roberta-base-sentiment-latest"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModelForSequenceClassification.from_pretrained(MODEL_NAME, use_safetensors=True)

# Sentiment label mapping
LABEL_MAP = {0: "negative", 1: "neutral", 2: "positive"}

def classify_sentiment(logits):
    """Convert model output to sentiment labels with high accuracy."""
    probs = torch.nn.functional.softmax(logits, dim=1)[0]
    predicted_label = torch.argmax(probs).item()
    
    sentiment = LABEL_MAP[predicted_label]

    # Check for extreme negativity (if negative probability > 95%)
    alert = "Extreme negative message detected!" if predicted_label == 0 and probs[0].item() > 0.95 else "no_alert"

    return sentiment, alert

@app.post("/analyze/")
async def analyze_text(input_data: InputText):
    inputs = tokenizer(input_data.text, return_tensors="pt", truncation=True, padding=True)

    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits

    sentiment, alert = classify_sentiment(logits)

    response = {
        "text": input_data.text,
        "sentiment": sentiment,
        "alert": alert
    }

    return response

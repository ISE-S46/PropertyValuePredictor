# Property Value Predictor

![GitHub repo size](https://img.shields.io/github/repo-size/ISE-S46/PropertyValuePredictor?cacheSeconds=60)
![GitHub Issues or Pull Requests](https://img.shields.io/github/issues/ISE-S46/PropertyValuePredictor?cacheSeconds=60)
![GitHub Issues or Pull Requests](https://img.shields.io/github/issues-pr/ISE-S46/PropertyValuePredictor?cacheSeconds=60)
![Version](https://img.shields.io/badge/version-1.0-blue)

**Property Value Predictor** - An android application built with **kotlin** for predicting property prices with **ONNX** machine learning model.

Based on [Aj. Ekarat project](https://www.youtube.com/watch?v=Qh5076U4-7s&list=PLCY_u0_oBT6kzzfsWqK7_hkXV6dc7eBtz&index=2), it only have rooms and area feature which could be improve apon, so this project added additional property types (Condo Townhouse, Detached) feature column to the given dataset to improve the app functionality.

**Note: This application is intended as a proof-of-concept and should not be used for real-world property value estimations.**

## Features

- **User input fields for area and numbers of room.**
- **Drop down for property types.**
- **Predicting property price with Onnx ML model.**
- **Scrollable view in horizonatl view**

## Requirements

- Python 3.8+
- Android Studio Narwhal | 2025.1.1 or Newer

## Installation

1. **Open project in Android Studio**

    1.1
    
    **At welcome page select Clone Repository**

    Or

    **At project page select file -> new -> Project From Version Control...**
    
    1.2. **Select git version control and paste the repository url, then click Clone**
    ```bash
    https://github.com/ISE-S46/PropertyValuePredictor.git
    ```
    1.3. **Wait for a few seconds for gradle to finished building.**

2. **Configure ONNX ML model**: (change python to py if it doesn't work)

    2.1 Create Python virtual environment, run these command in Android Studio Terminal.
    ```bash
    python -m venv Model
    Model\Scripts\activate
    ```

    2.2 Run these commands
    ```bash
    cd ./Model_training
    pip install -r requirement.txt
    python TrainModel.py
    ```

    2.3 Create /raw folder at /app/src/main/res
    ```bash
    cd ..
    cd ./app/src/main/res
    mkdir raw
    ```

    2.4 Copy/Cut house_price_model.onnx to ./app/src/main/res/raw folder
    - Set from Android to Project at top left to find **house_price_model.onnx**
    - Copy house_price_model.onnx at ./Train_Model folder
    - Paste at ./app/src/main/res/raw folder

3. **Run the application**:

    App Background image from [Unsplash](https://unsplash.com/photos/orange-and-gray-concrete-house-surround-by-snow-Sv4btqhcYqw) 
    - **The application should look like this if there is no error**:

        ![App](/READMEimg/App.png)

    - **Example Prediction**:

        ![App](/READMEimg/AppSuccess.png)

## Contributing

Contributions are welcome! If you have suggestions for improvements, new features, or bug fixes, please feel free to fork the repository and open a pull request.

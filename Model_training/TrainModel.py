import pandas as pd
import numpy as np
from sklearn.linear_model import LinearRegression
from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType

df = pd.read_csv("./data.txt")
array = df.values
X = array[:,0:3]
y = array[:,3]

Lr = LinearRegression()
Lr.fit(X,y)

initial_type = [
    ('input', FloatTensorType([None, 3]))
]
converted_model = convert_sklearn(Lr, initial_types = initial_type)
with open("house_price_model.onnx", "wb") as f:
    f.write(converted_model.SerializeToString())
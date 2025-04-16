import React from "react";

interface InputFieldProps {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
}

const InputField: React.FC<InputFieldProps> = ({ value, onChange, placeholder }) => {
  return (
    <input
      type="text"
      value={value}
      onChange={onChange}
      placeholder={placeholder || "Enter text..."}
      className="flex-1 p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
    />
  );
};

export default InputField;

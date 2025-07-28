import React, { useEffect, useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Box,
} from '@mui/material';
import { use } from 'react';

const categoryOptions = [
  'FOOD',
  'TRANSPORT',
  'SHOPPING',
  'UTILITIES',
  'ENTERTAINMENT',
  'HEALTHCARE',
  'GROCERIES',
  'EDUCATION',
  'OTHER'
];

const paymentMethodOptions = [
  'CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'DIGITAL_WALLET','UPI', 'CHEQUE', 'SALARY', 'OTHER'
];

const transactionTypeOptions = [
  'CREDIT', 'DEBIT'
];

const AddExpense = ({ open, onClose, onSave, initialData = {} }) => {
  const [form, setForm] = useState({
    id: initialData.id || '',
    amount: initialData.amount || '',
    category: initialData.category || '',
    createdAt: new Date().toISOString(),
    date: initialData.date || '',
    description: initialData.description || '',
    location: initialData.location || '',
    paymentMethod: initialData.paymentMethod || '',
    transactionType: initialData.transactionType || '',
    userId: localStorage.getItem('userId') || '1'
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    setForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
    //if value is present, remove the error for that field
    if (errors[e.target.name]) {
      setErrors((prev) => ({
        ...prev,
        [e.target.name]: '',
      }));
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!form.amount) newErrors.amount = 'Amount is required';
    if (!form.category) newErrors.category = 'Category is required';
    if (!form.date) newErrors.date = 'Date is required';
    if (!form.description) newErrors.description = 'Description is required';
    if (!form.paymentMethod) newErrors.paymentMethod = 'Payment method is required';
    if (!form.transactionType) newErrors.transactionType = 'Transaction type is required';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = () => {
    if (!validate()) return;

    
    form.userId = localStorage.getItem('userId') || '0';

    onSave(form);
    // Reset form and errors after saving
    setForm(initialData);
    setErrors({});
    // Close the dialog
    onClose();
  };

  const handleClose = () => {
    setForm(initialData);
    setErrors({});
    onClose();
  };

  useEffect(() => {
    setForm({
      id: initialData.id || '',
      amount: initialData.amount || '',
      category: initialData.category || '',
      createdAt: new Date().toISOString(),
      date: initialData.date || '',
      description: initialData.description || '',
      location: initialData.location || '',
      paymentMethod: initialData.paymentMethod || '',
      transactionType: initialData.transactionType || '',
      userId: localStorage.getItem('userId') || '1'
    });
  }, [initialData]);

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle>Add Expense</DialogTitle>
      <DialogContent>
        <input type="hidden" name="id" value={form.id} />

        <Box
          display="flex"
          flexWrap="wrap"
          gap={2}
          mt={1}
          sx={{ '& > *': { flex: '1 1 48%' }, '& > .fullWidth': { flex: '1 1 100%' } }}
        >
          <TextField
            label="Amount"
            name="amount"
            value={form.amount}
            onChange={handleChange}
            error={!!errors.amount}
            helperText={errors.amount}
          />

          <TextField
            label="Category"
            name="category"
            select
            value={form.category || ''}
            onChange={handleChange}
            error={!!errors.category}
            helperText={errors.category}
          >
            {categoryOptions.map((option) => (
              <MenuItem key={option} value={option}>
                {option}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="Date"
            name="date"
            type="datetime-local"
            value={form.date ? form.date.substring(0, 16) : ''}
            onChange={(e) => {
              handleChange({ target: { name: 'date', value: e.target.value } });
            }}
            slotProps={{ inputLabel: { shrink: true } }}
            error={!!errors.date}
            helperText={errors.date}
          />

          <TextField
            label="Location"
            name="location"
            value={form.location}
            onChange={handleChange}
          />

          <TextField
            className="fullWidth"
            label="Description"
            multiline
            rows={2}
            name="description"
            value={form.description}
            onChange={handleChange}
            error={!!errors.description}
            helperText={errors.description}
          />

          <TextField
            label="Payment Method"
            name="paymentMethod"
            select
            value={form.paymentMethod || ''}
            onChange={handleChange}
            error={!!errors.paymentMethod}
            helperText={errors.paymentMethod}
          >
            {paymentMethodOptions.map((option) => (
              <MenuItem key={option} value={option}>
                {option}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="Transaction Type"
            name="transactionType"
            select
            value={form.transactionType || ''}
            onChange={handleChange}
            error={!!errors.transactionType}
            helperText={errors.transactionType}
          >
            {transactionTypeOptions.map((option) => (
              <MenuItem key={option} value={option}>
                {option}
              </MenuItem>
            ))}
          </TextField>
        </Box>
      </DialogContent>

      <DialogActions>
        <Button onClick={handleSubmit} variant="outlined">
          Submit
        </Button>
        <Button onClick={handleClose} variant="outlined">
          Cancel
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AddExpense;

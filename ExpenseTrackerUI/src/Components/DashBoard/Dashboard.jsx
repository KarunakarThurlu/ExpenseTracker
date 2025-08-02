import React, { useEffect, useState } from 'react';
import {
  Card, CardContent, Typography, Box, Table, TableBody, TableCell,
  TableHead, TableRow, TextField, Button, useTheme
} from '@mui/material';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import { useLazyQuery } from '@apollo/client';
import { DASHBOARD_QUERY } from '../../Graphql/Mutations/CommonMutation';
import dayjs from 'dayjs';

// Chart Config
const getStyledAreaSplineChart = (title, dataSet = [], color = '#fbc600', isCount = false, themeMode = 'light') => ({
  chart: {
    type: 'areaspline',
    backgroundColor: 'transparent',
    style: { fontFamily: "'Roboto', sans-serif" },
  },
  title: {
    text: title,
    style: { color: themeMode === 'dark' ? '#fff' : '#333', fontWeight: '600', fontSize: '18px' }
  },
  xAxis: {
    categories: dataSet.map(d => d.key),
    tickmarkPlacement: 'on',
    title: { enabled: false },
    labels: { style: { color: themeMode === 'dark' ? '#ccc' : '#666' } }
  },
  yAxis: {
    title: {
      text: isCount ? 'Count' : 'Amount (₹)',
      style: { color: themeMode === 'dark' ? '#ccc' : '#666' }
    },
    labels: {
      style: { color: themeMode === 'dark' ? '#ccc' : '#666' },
      formatter: function () {
        return isCount ? this.value : `₹ ${this.value}`;
      }
    },
    gridLineColor: themeMode === 'dark' ? '#444' : '#eee'
  },
  tooltip: {
    shared: true,
    valuePrefix: isCount ? '' : '₹ '
  },
  plotOptions: {
    areaspline: {
      fillColor: {
        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
        stops: [
          [0, Highcharts.color(color).setOpacity(0.4).get('rgba')],
          [1, Highcharts.color(color).setOpacity(0).get('rgba')]
        ]
      },
      lineColor: color,
      lineWidth: 2,
      marker: { enabled: true, radius: 4, symbol: 'circle' }
    }
  },
  series: [{
    name: title,
    color,
    data: dataSet.map(d => isCount ? parseInt(d.value, 10) : parseFloat(d.value))
  }],
  credits: { enabled: false },
});

const getDefaultDateRange = () => {
  const now = new Date();
  const first = new Date(now.getFullYear(), now.getMonth(), 1);
  const last = new Date(now.getFullYear(), now.getMonth() + 1, 0);
  return {
    from: first.toISOString(),
    to: last.toISOString()
  };
};

const Dashboard = () => {
  const theme = useTheme();
  const mode = theme.palette.mode;

  const { from, to } = getDefaultDateRange();
  const [fromDate, setFromDate] = useState(from);
  const [toDate, setToDate] = useState(to);
  const [fetchDashboard, { loading, error, data }] = useLazyQuery(DASHBOARD_QUERY);

  useEffect(() => {
    const startOfMonth = dayjs().startOf('month').format('YYYY-MM-DD');
    const endOfMonth = dayjs().endOf('month').format('YYYY-MM-DD');
    setFromDate(startOfMonth);
    setToDate(endOfMonth);
    fetchDashboard({ variables: { fromDate: `${startOfMonth}T00:00:00.000Z`, toDate: `${endOfMonth}T23:59:59.999Z` } });
  }, []);

  const handleSearch = () => {
    fetchDashboard({
      variables: {
        fromDate: `${fromDate}T00:00:00.000Z`,
        toDate: `${toDate}T23:59:59.999Z`
      }
    });
  };

  if (loading) return <p>Loading dashboard...</p>;
  if (error) return <p>Error loading dashboard.</p>;
  if (!data?.dashboardData) return null;

  const dashboardData = data.dashboardData;
  const credit = parseFloat(dashboardData.transactionTypeByAmount?.find(t => t.key === 'CREDIT')?.value || 0);
  const debit = parseFloat(dashboardData.transactionTypeByAmount?.find(t => t.key === 'DEBIT')?.value || 0);
  const net = credit - debit;

  const shadowStyle = {
    borderRadius: 3,
    boxShadow: theme.shadows[3],
    backgroundColor: theme.palette.background.paper,
    p: 2,
    transition: 'transform 0.2s ease',
    '&:hover': {
      transform: 'scale(1.01)',
      boxShadow: theme.shadows[6]
    }
  };

  return (
    <Box p={3} display="flex" flexDirection="column" gap={2}>
      <Box display="flex" alignItems="center" justifyContent="space-between" flexWrap="wrap" mb={1}>
        <Typography variant="h5" fontWeight="bold">Expense Dashboard</Typography>
        <Box display="flex" alignItems="center" gap={1} mt={{ xs: 1, sm: 0 }}>
          <TextField
            type="date"
            name='from'
            size="small"
            label="From"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            type="date"
            name='to'
            size="small"
            label="To"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
          <Button variant="outlined" onClick={handleSearch}>
            Apply
          </Button>
        </Box>
      </Box>

      <Box display="grid" gridTemplateColumns={{ xs: '1fr', sm: '1fr 1fr 1fr' }} gap={2}>
        <Card sx={shadowStyle}><CardContent><Typography variant="h6">Credits</Typography><Typography variant="h5">₹ {credit.toFixed(2)}</Typography></CardContent></Card>
        <Card sx={shadowStyle}><CardContent><Typography variant="h6">Debits</Typography><Typography variant="h5">₹ {debit.toFixed(2)}</Typography></CardContent></Card>
        <Card sx={shadowStyle}><CardContent><Typography variant="h6">Net</Typography><Typography variant="h5">₹ {net.toFixed(2)}</Typography></CardContent></Card>
      </Box>

      <Box display="grid" gridTemplateColumns={{ xs: '1fr', md: '1fr 1fr' }} gap={2}>
        <Box sx={shadowStyle}>
          <HighchartsReact
            highcharts={Highcharts}
            options={getStyledAreaSplineChart('Amount by Category', dashboardData.groupingByCategory, '#fbc600', false, mode)}
          />
        </Box>
        <Box sx={shadowStyle}>
          <HighchartsReact
            highcharts={Highcharts}
            options={getStyledAreaSplineChart('Transaction Count by Payment Method', dashboardData.groupingByPaymentMethod, '#fbc600', true, mode)}
          />
        </Box>
      </Box>

      <Card sx={shadowStyle}>
        <CardContent>
          <Typography variant="h6" gutterBottom>Last 5 Transactions</Typography>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell><strong>TransactionType</strong></TableCell>
                <TableCell><strong>Amount</strong></TableCell>
                <TableCell><strong>Category</strong></TableCell>
                <TableCell><strong>PaymentMethod</strong></TableCell>
                <TableCell><strong>TransactionDate</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {dashboardData.lastFiveTransactions?.map((tx, idx) => (
                <TableRow key={idx}>
                  <TableCell>{tx.transactionType}</TableCell>
                  <TableCell>₹ {parseFloat(tx.amount).toFixed(2)}</TableCell>
                  <TableCell>{tx.category}</TableCell>
                  <TableCell>{tx.paymentMethod}</TableCell>
                  <TableCell>{new Date(tx.date).toLocaleDateString()}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Dashboard;

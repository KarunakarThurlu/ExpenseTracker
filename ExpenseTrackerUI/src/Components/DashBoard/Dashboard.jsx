import React, { useEffect, useState } from 'react';
import {
  Card, CardContent, Typography, Box, Table, TableBody, TableCell,
  TableHead, TableRow, TextField, Button
} from '@mui/material';
import Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import { gql, useLazyQuery } from '@apollo/client';

// GraphQL Query
const DASHBOARD_QUERY = gql`
  query DashboardData($fromDate: String!, $toDate: String!) {
    dashboardData(fromDate: $fromDate, toDate: $toDate) {
      transactionTypeByAmount { key value }
      groupingByCategory { key value }
      groupingByPaymentMethod { key value }
      lastFiveTransactions {
        amount category createdAt date paymentMethod transactionType
      }
    }
  }
`;

// Chart Config
const getStyledAreaSplineChart = (title, dataSet, color = '#fbc600', isCount = false) => ({
  chart: { type: 'areaspline', backgroundColor: 'transparent', style: { fontFamily: "'Roboto', sans-serif" } },
  title: { text: title, style: { color: '#333', fontWeight: '600', fontSize: '18px' } },
  xAxis: {
    categories: dataSet.map(d => d.key),
    tickmarkPlacement: 'on',
    title: { enabled: false },
    labels: { style: { color: '#666' } }
  },
  yAxis: {
    title: {
      text: isCount ? 'Count' : 'Amount (₹)', style: { color: '#666' }
    },
    labels: {
      style: { color: '#666' },
      formatter: function () {
        return isCount ? this.value : `₹ ${this.value}`;
      }
    },
    gridLineColor: '#eee'
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

// Util to get first and last day of current month
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
  const { from, to } = getDefaultDateRange();
  const [fromDate, setFromDate] = useState(from);
  const [toDate, setToDate] = useState(to);

  const [fetchDashboard, { loading, error, data }] = useLazyQuery(DASHBOARD_QUERY);

  useEffect(() => {
    fetchDashboard({ variables: { fromDate, toDate } });
  }, []);

  const handleSearch = () => {
    const fromDateTime =`${fromDate}T00:00:00.000Z`
    const toDateTime = `${toDate}T23:59:59.999Z`
    //2025-07-28T00:00  "2025-07-31T23:59"
    fetchDashboard({ variables: { fromDate:fromDateTime, toDate:toDateTime }});
  };

const handleDateSelect = (e) => {
  if (e.name === 'from') {
    const fromDateTime = new Date(`${fromDate}T00:00:00.000Z`).toISOString()
    setFromDate(fromDateTime)
  } else {
    const toDateTime = new Date(`${toDate}T00:00:00.000Z`).toISOString()
    setFromDate(toDateTime)
  }
  console.log(e.target.value)
}

if (loading) return <p>Loading dashboard...</p>;
if (error) return <p>Error loading dashboard.</p>;
if (!data) return null;

const dashboardData = data.dashboardData;
const credit = parseFloat(dashboardData.transactionTypeByAmount.find(t => t.key === 'CREDIT')?.value || 0);
const debit = parseFloat(dashboardData.transactionTypeByAmount.find(t => t.key === 'DEBIT')?.value || 0);
const net = credit - debit;

const shadowStyle = {
  borderRadius: 3,
  boxShadow: '0 10px 30px rgba(0,0,0,0.15)',
  backgroundColor: '#fff',
  p: 2,
  transition: 'transform 0.2s ease',
  '&:hover': {
    transform: 'scale(1.01)',
    boxShadow: '0 12px 40px rgba(0,0,0,0.2)'
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
        <Button variant="outlined" onClick={handleSearch} sx={{ color: '#000', fontWeight: 600 }}>
          Apply
        </Button>
      </Box>
    </Box>

    <Box display="grid" gridTemplateColumns={{ xs: '1fr', sm: '1fr 1fr 1fr' }} gap={2}>
      <Card sx={shadowStyle}><CardContent><Typography variant="h6">Credits</Typography><Typography variant="h5">₹ {credit.toFixed(2)}</Typography></CardContent></Card>
      <Card sx={shadowStyle}><CardContent><Typography variant="h6">Debits</Typography><Typography variant="h5">₹ {debit.toFixed(2)}</Typography></CardContent></Card>
      <Card sx={shadowStyle}><CardContent><Typography variant="h6">Net</Typography><Typography variant="h5">₹ {net.toFixed(2)}</Typography></CardContent></Card>
    </Box>

    {/* Responsive chart layout */}
    <Box
      display="grid"
      gridTemplateColumns={{ xs: '1fr', md: '1fr 1fr' }}
      gap={2}
    >
      <Box sx={shadowStyle}>
        <HighchartsReact
          highcharts={Highcharts}
          options={getStyledAreaSplineChart('Amount by Category', dashboardData.groupingByCategory)}
        />
      </Box>
      <Box sx={shadowStyle}>
        <HighchartsReact
          highcharts={Highcharts}
          options={getStyledAreaSplineChart('Transaction Count by Payment Method', dashboardData.groupingByPaymentMethod, '#fbc600', true)}
        />
      </Box>
    </Box>

    <Card sx={shadowStyle}>
      <CardContent>
        <Typography variant="h6" gutterBottom>Last 5 Transactions</Typography>
        <Table size="small">
          <TableHead>
            <TableRow sx={{ backgroundColor: '#fff8e1' }}>
              <TableCell><strong>Type</strong></TableCell>
              <TableCell><strong>Amount</strong></TableCell>
              <TableCell><strong>Category</strong></TableCell>
              <TableCell><strong>Method</strong></TableCell>
              <TableCell><strong>Date</strong></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {dashboardData.lastFiveTransactions.map((tx, idx) => (
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

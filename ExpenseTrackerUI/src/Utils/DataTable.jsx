import React, { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { useNotifier } from './Notifyer';

const DataTable = ({
  columns,
  fetchData,
  initialSort = { field: 'createdAt', sort: 'desc' },
  pageSizeOptions = [5, 10, 20],
}) => {
  const { showNotification } = useNotifier();
  const [paginationModel, setPaginationModel] = useState({ page: 0, pageSize: 5 });
  const [sortModel, setSortModel] = useState([initialSort]);
  const [rows, setRows] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    setLoading(true);
    try {
      const { data, total } = await fetchData({
        pageSize: paginationModel.pageSize,
        pageNumber: paginationModel.page,
        sortBy: sortModel[0]?.field,
        sortDirection: sortModel[0]?.sort?.toUpperCase() || 'DESC',
      });
      setRows(data);
      setTotal(total);
    } catch (err) {
      console.log('DataTable fetch error:', err);
      showNotification(err.message, 'error')
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, [paginationModel, sortModel]);

  return (
    <DataGrid
      rows={rows}
      columns={columns}
      loading={loading}
      pagination
      paginationModel={paginationModel}
      onPaginationModelChange={setPaginationModel}
      paginationMode="server"
      rowCount={total}
      pageSizeOptions={pageSizeOptions}
      sortingMode="server"
      sortModel={sortModel}
      onSortModelChange={setSortModel}
      sx={{
        height: 600, // ✅ fixes total height, including header + footer
        '& .MuiDataGrid-columnHeader': {
          backgroundColor: '#fbc600',
        },
        '& .MuiDataGrid-virtualScroller': {
          overflowY: 'auto', // ✅ scroll inside for rows
        },
        '& .MuiDataGrid-footerContainer': {
          backgroundColor: '#fbc600',
          fontSize:'5px'
        },
      }}
    />

  );
};

export default DataTable;

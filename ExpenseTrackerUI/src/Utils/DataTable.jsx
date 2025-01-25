import React, { useEffect, useState } from 'react';
import { DataGrid } from '@mui/x-data-grid';

const DataTable = ({
  columns,
  fetchData, // Function to fetch data from the server
  pageSizeOptions = [5, 10, 25, 50],
}) => {
  const [rows, setRows] = useState([]);
  const [rowCount, setRowCount] = useState(0);
  const [loading, setLoading] = useState(false);

  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: pageSizeOptions[0],
  });


  useEffect(() => {
    const fetchTableData = async () => {
      setLoading(true);
      try {
        const data = await fetchData({
          page: paginationModel.page,
          pageSize: paginationModel.pageSize
        });

        setRows(data.rows);
        setRowCount(data.total);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTableData();
  }, [paginationModel]);

  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        rowCount={rowCount}
        pageSizeOptions={pageSizeOptions}
        paginationMode="server"
        sortingMode="server"
        paginationModel={paginationModel}
        onPaginationModelChange={(newModel) => setPaginationModel(newModel)}
        loading={loading}
      />
    </div>
  );
};

export default DataTable;

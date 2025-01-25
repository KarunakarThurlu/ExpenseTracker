import { Box } from "@mui/material";
import DataTable from "../Utils/DataTable"


const Expenses = () => {
    //Table Name left side, from ,to date fields and + Icon on right side in single line 

    //Graphql query to fetch data with pageNumber, pageSize
    const fetchUserData=(pageNumber,pageSize)=>{

    }

    return (
        <>
        <Box>
            {/* //Table Name left side, from ,to date fields and + Icon on right side in single line */}
            
        </Box>
            <DataTable columns={[]} fetchData={fetchUserData}/>
        </>
    )
}

export default Expenses;
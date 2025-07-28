package com.expensetracker.util;

public class QueryConstants {
	public static final String EXPENSES_QUERY_FOR_DASHBOARD="""
			SELECT 'TRANSACTION_TYPE' AS type, transaction_type AS key, SUM(amount) AS value
			FROM expenses
			WHERE created_at BETWEEN :from AND :to
			GROUP BY transaction_type

			UNION ALL

			SELECT 'CATEGORY', category, sum(amount)::decimal
			FROM expenses
			WHERE created_at BETWEEN :from AND :to
			GROUP BY category

			UNION ALL

			SELECT 'PAYMENT_METHOD', payment_method, COUNT(*)::decimal
			FROM expenses
			WHERE created_at BETWEEN :from AND :to
			GROUP BY payment_method
			""";
	public static final String LAST_FIVE_TRANSACTIONS="""
			SELECT 
					category, amount, payment_method, transaction_Type, date 
			FROM 
					expenses
			WHERE 
					user_id=:userID
			ORDER BY
			        created_at DESC
			LIMIT 
					5
			""";
}

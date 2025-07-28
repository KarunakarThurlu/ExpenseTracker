// src/apolloClient.js or wherever you define this

import {
  ApolloClient,
  InMemoryCache,
  ApolloProvider,
  createHttpLink,
} from '@apollo/client';
import { setContext } from '@apollo/client/link/context';

// 1. Create the HTTP link to your GraphQL server
const httpLink = createHttpLink({
  uri: 'http://localhost:2024/graphql',
});

// 2. Create an authLink that injects the Authorization header
const authLink = setContext((_, { headers }) => {
  const token = localStorage.getItem('token'); // get token from localStorage, cookie, or context
  return {
    headers: {
      ...headers,
      Authorization: token ? `Bearer ${token}` : '',
    },
  };
});

// 3. Compose authLink + httpLink in ApolloClient
const client = new ApolloClient({
  link: authLink.concat(httpLink),
  cache: new InMemoryCache(),
});

// 4. Provide it to your app
const ApolloClientProvider = ({ children }) => (
  <ApolloProvider client={client}>
    {children}
  </ApolloProvider>
);

export default ApolloClientProvider;
export { client as apolloClient };
export { ApolloClientProvider };

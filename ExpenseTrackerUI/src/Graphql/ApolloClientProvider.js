import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:2024/graphql', // Backend GraphQL endpoint
  cache: new InMemoryCache(),
});

export const ApolloClientProvider = ({ children }) => (
  <ApolloProvider client={client}>
    {children}
  </ApolloProvider>
);

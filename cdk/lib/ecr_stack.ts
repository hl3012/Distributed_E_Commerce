import * as cdk from 'aws-cdk-lib';
import * as ecr from 'aws-cdk-lib/aws-ecr';
import { Construct } from 'constructs';

interface EcrStackProps extends cdk.StackProps {
    repositoryName: string;
}

export class EcrStack extends cdk.Stack {
    public readonly repository: ecr.Repository;

    constructor(scope: Construct, id: string, props: EcrStackProps) {
        super(scope, id, props);

        this.repository = new ecr.Repository(this, 'EcrRepository', {
            repositoryName: props.repositoryName,
            removalPolicy: cdk.RemovalPolicy.DESTROY,
            autoDeleteImages: true
        });

        new cdk.CfnOutput(this, 'EcrRepositoryUri', {
            value: this.repository.repositoryUri
        });
    }
}